package com.ren.framework.security.utils;

import com.ren.framework.properties.TokenProperties;
import com.ren.common.core.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {
    @Autowired
    private TokenProperties tokenProperties;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 密钥生成
    private Key getAccessKey() {
        return Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes());
    }

    private Key getRefreshKey() {
        return Keys.hmacShaKeyFor(tokenProperties.getRefreshSecret().getBytes());
    }

    /*
     * 生成AccessToken
     * @param userDetails
     * @return java.lang.String
     * @author admin
     * @date 2025/04/17 21:24
     */
    public String createAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())  //设置JWT的主题为用户名的字符串
                .claim("user_id", ((User) userDetails).getUserId())  //添加一个自定义声明user_id，值为用户的ID
                .claim("authorities", userDetails.getAuthorities())  //添加权限信息作为另一个自定义声明
                .setIssuedAt(new Date())  //设置JWT的签发时间为当前时间
                .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getExpireTime() * 1000L))  //设置过期时间，基于当前时间加上配置的过期时长
                .signWith(getAccessKey(), SignatureAlgorithm.HS512)  //使用HS512算法和密钥进行签名
                .compact();  //生成最终的JWT字符串
    }

    /*
     * 生成RefreshToken（只包含用户ID和用户名）
     * @param userDetails
     * @return java.lang.String
     * @author admin
     * @date 2025/04/17 21:24
     */
    public String createRefreshToken(UserDetails userDetails) {
        String refreshToken = Jwts.builder()
                .setSubject(userDetails.getUsername())  //设置JWT的主题为用户名的字符串
                .claim("user_id", ((User) userDetails).getUserId())  //添加一个自定义声明user_id，值为用户的ID
                .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getRefreshExpireTime() * 1000L))  //设置过期时间，基于当前时间加上配置的过期时长
                .signWith(getRefreshKey(), SignatureAlgorithm.HS512)  //使用HS512算法和密钥进行签名
                .compact();  //生成最终的JWT字符串

        // 存储到Redis，有效期比Token长一些，key为refresh:+用户id，value为refreshToken，有效期为配置的过期时长+60秒（为了防止早删除），时间单位为秒
        redisTemplate.opsForValue().set(
                "refresh:" + ((User) userDetails).getUserId(),
                refreshToken,
                tokenProperties.getRefreshExpireTime() + 60,
                TimeUnit.SECONDS
        );
        return refreshToken;
    }

    /*
     * 从请求头中提取AccessToken
     * @param request
     * @return java.lang.String
     * @author admin
     * @date 2025/04/17 21:24
     */
    public String getAccessToken(HttpServletRequest request) {
        //根据请求头中的X-Access-Token字段来获取token
        String header = request.getHeader("X-Access-Token");
        //判断是否有X-Access-Token字段，并且X-Access-Token字段以Bearer开头，如果是，则截取掉Bearer ，之后返回token
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    /*
     * 从请求头中提取RefreshToken
     * @param request
     * @return java.lang.String
     * @author admin
     * @date 2025/04/17 21:24
     */
    public String getRefreshToken(HttpServletRequest request) {
        //根据请求头中的X-Refresh-Token字段来获取token
        return request.getHeader("X-Refresh-Token");
    }

    /*
     * 从AccessToken中解析出Claims
     * Claims 是 Token 的有效载荷（Payload）部分，用于携带身份、权限或其他自定义数据。可以将 Claims 理解为 JWT 中存储的“声明”或“信息片段”
     * @param token
     * @return io.jsonwebtoken.Claims
     * @author admin
     * @date 2025/04/17 21:24
     */
    private Claims parseAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getAccessKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /*
     * 从RefreshToken中提取出Claims
     * Claims 是 Token 的有效载荷（Payload）部分，用于携带身份、权限或其他自定义数据。可以将 Claims 理解为 JWT 中存储的“声明”或“信息片段”
     * @param token
     * @return io.jsonwebtoken.Claims
     * @author admin
     * @date 2025/04/17 21:25
     */
    public Claims parseRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getRefreshKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /*
     * 验证AccessToken（包括AccessToken是否过期和AccessToken是否有效）
     * Claims 是 Token 的有效载荷（Payload）部分，用于携带身份、权限或其他自定义数据。可以将 Claims 理解为 JWT 中存储的“声明”或“信息片段”
     * 返回值200：表示正常，有效    返回值403：表示黑名单    返回值401：表示已过期    返回值422：表示Token无效
     * @param token
     * @return short
     * @author admin
     * @date 2025/04/17 21:25
     */
    public short validateAccessToken(String token) {
        // 从token中解析出user_id
        Claims claims = parseAccessToken(token);
        Long userId = claims.get("user_id", Long.class);

        // 检查黑名单中是否存在当前token，如果存在，则无效，反之则有效
        if (redisTemplate.hasKey("blacklist:" + token)) {
            // 黑名单 → 返回 403
            log.warn("当前Token处于黑名单，请确认");
            return 403;
        }

        return 200;
    }

    /*
     * 验证RefreshToken（检查Redis中的存储）
     * Claims 是 Token 的有效载荷（Payload）部分，用于携带身份、权限或其他自定义数据。可以将 Claims 理解为 JWT 中存储的“声明”或“信息片段”
     * @param refreshToken
     * @return boolean
     * @author admin
     * @date 2025/04/17 21:25
     */
    public boolean validateRefreshToken(String refreshToken) {
        // 从token中解析出user_id
        Claims claims = parseRefreshToken(refreshToken);
        Long userId = claims.get("user_id", Long.class);
        //从redis中获取对应的refreshToken
        String storedToken = (String) redisTemplate.opsForValue().get("refresh:" + userId);
        //如果redis中的refreshToken与传递过来的refreshToken一致，则有效，反之则无效
        return refreshToken.equals(storedToken);
    }

    /*
     * 判断是否需要刷新Token
     * Claims 是 Token 的有效载荷（Payload）部分，用于携带身份、权限或其他自定义数据。可以将 Claims 理解为 JWT 中存储的“声明”或“信息片段”
     * @param token
     * @return boolean
     * @author admin
     * @date 2025/04/17 21:26
     */
    public boolean shouldRefreshToken(String token) {
        // 从token中解析出过期时间
        Claims claims = parseAccessToken(token);
        Date expiration = claims.getExpiration();
        // 剩余时间 = 过期时间-当前时间（单位：秒）
        long remainTime = expiration.getTime() - System.currentTimeMillis();
        // 如果剩余时间小于刷新时间，则需要刷新Token
        return remainTime < tokenProperties.getRefreshTime() * 1000L;
    }

    /*
     * 从JWT令牌中提取用户身份和权限信息，构建Spring Security认证对象（Authentication）
     * Claims 是 Token 的有效载荷（Payload）部分，用于携带身份、权限或其他自定义数据。可以将 Claims 理解为 JWT 中存储的“声明”或“信息片段”
     * 返回SpringSecurity的认证信息对象，Spring Security后续通过SecurityContextHolder获取当前用户身份，用于：鉴权（如@PreAuthorize("hasRole('ADMIN')")），获取用户信息（如@AuthenticationPrincipal User user）
     * @param token
     * @return org.springframework.security.core.Authentication
     * @author admin
     * @date 2025/04/17 21:26
     */
    public Authentication getAuthentication(String token) {
        // 从token中解析出user_id和username
        Claims claims = parseAccessToken(token);
        Long userId = claims.get("user_id", Long.class);
        String username = claims.getSubject();

        // 从claims中提取权限信息
        List<String> authorities = claims.get("authorities", List.class);
        // 将权限列表转换为SpringSecurity可以认识的权限列表（GrantedAuthority列表）
        List<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 构建User对象（根据实际情况调整）
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setRoles(authorities); // 假设roles字段存储权限列表

        return new UsernamePasswordAuthenticationToken(
                user,
                null, // 凭证置空
                grantedAuthorities
        );
    }

    /*
     * 删除RefreshToken（登出时调用）
     * @param userId
     * @author admin
     * @date 2025/04/17 21:26
     */
    public void deleteRefreshToken(Long userId) {
        //从redis中删除refreshToken
        redisTemplate.delete("refresh:" + userId);
    }


    public static void main(String[] args) {
        // 生成 64 字节（512 位）的 HS512 密钥
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        // 转换为 Base64 字符串存储
        String base64Key = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("安全密钥: " + base64Key);
    }
}