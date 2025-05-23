package com.ren.framework.security.utils;

import com.alibaba.fastjson2.JSON;
import com.ren.common.domain.bo.LoginUser;
import com.ren.common.utils.FastJSON2Utils;
import com.ren.framework.properties.TokenProperties;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtUtils {
    @Autowired
    private TokenProperties tokenProperties;
    //这里本来可以使用StringRedisTemplate，因为这里存储的时简单的字符串，但是为了项目统一，所以放弃，整个项目都使用配置后的RedisTemplate<String, Object>
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
     * @param loginUser
     * @param expireTime 过期时间，毫秒时间戳
     * @return java.lang.String
     * @author admin
     * @date 2025/04/17 21:24
     */
    public String createAccessToken(LoginUser loginUser,Long expireTime) {
        expireTime = expireTime == null ? (System.currentTimeMillis() + tokenProperties.getExpireTime() * 1000L) : expireTime;
        String login_user = FastJSON2Utils.toString(loginUser);
        //过滤敏感字段
        //String login_user = FastJSON2Utils.filterSensitiveFields(FastJSON2Utils.EXCLUDE_PROPERTIES,loginUser);
        return Jwts.builder()
                .setSubject(loginUser.getUsername())  //设置JWT的主题为用户名的字符串
                .claim("user_id", loginUser.getUserId())  //添加一个自定义声明user_id，值为用户的ID
                .claim("login_user", login_user)  //将login_user整个对象序列化后放入自定义声明，方便后面使用
                .setIssuedAt(new Date())  //设置JWT的签发时间为当前时间
                .setExpiration(new Date(expireTime))  //设置过期时间，基于当前时间加上配置的过期时长
                .signWith(getAccessKey(), SignatureAlgorithm.HS512)  //使用HS512算法和密钥进行签名
                .compact();  //生成最终的JWT字符串
    }

    /*
     * 生成RefreshToken（只包含用户ID和用户名）
     * @param user
     * @return java.lang.String
     * @author admin
     * @date 2025/04/17 21:24
     */
    public String createRefreshToken(LoginUser loginUser,Long expireTime) {
        expireTime = expireTime == null ? (System.currentTimeMillis() + tokenProperties.getRefreshExpireTime() * 1000L) : expireTime;
        String login_user = FastJSON2Utils.toString(loginUser);
        //过滤敏感字段
        //String login_user = FastJSON2Utils.filterSensitiveFields(FastJSON2Utils.EXCLUDE_PROPERTIES,loginUser);
        String refreshToken = Jwts.builder()
                .setSubject(loginUser.getUsername())  //设置JWT的主题为用户名的字符串
                .claim("user_id", loginUser.getUserId())  //添加一个自定义声明user_id，值为用户的ID
                .claim("login_user", login_user)  //将login_user整个对象序列化后放入自定义声明，方便后面使用
                .setExpiration(new Date(expireTime))  //设置过期时间，基于当前时间加上配置的过期时长
                .signWith(getRefreshKey(), SignatureAlgorithm.HS512)  //使用HS512算法和密钥进行签名
                .compact();  //生成最终的JWT字符串

        // 存储到Redis，有效期比Token长一些，key为refresh:+用户id，value为refreshToken，有效期为配置的过期时长+60秒（为了防止早删除），时间单位为秒
        redisTemplate.opsForValue().set(
                "refresh:" + loginUser.getUserId(),
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
     * 返回SpringSecurity的认证信息对象，Spring Security后续通过SecurityContextHolder获取当前用户身份，用于：鉴权（如@PreAuthorize("hasRole('ADMIN')")），获取用户信息（如@AuthenticationPrincipal LoginUser user）
     * @param token
     * @return org.springframework.security.core.Authentication
     * @author admin
     * @date 2025/04/17 21:26
     */
    public Authentication getAuthenticationByAccessToken(String token) {
        // 从token中解析出loginUserJson
        Claims claims = parseAccessToken(token);
        // 将loginUserJson解析为LoginUser对象
        String loginUserJson = claims.get("login_user", String.class);
        LoginUser loginUser = JSON.parseObject(loginUserJson, LoginUser.class);

        return new UsernamePasswordAuthenticationToken(
                loginUser,
                null, // 凭证置空
                loginUser.getAuthorities()
        );
    }

    /*
     * 从 RefreshToken 解析出Authentication
     * @param token
     * @return org.springframework.security.core.Authentication
     * @author admin
     * @date 2025/05/21 09:42
     */
    public Authentication getAuthenticationByRefreshToken(String token) {
        // 从token中解析出loginUserJson
        Claims claims = parseRefreshToken(token);
        // 将loginUserJson解析为LoginUser对象
        String loginUserJson = claims.get("login_user", String.class);
        LoginUser loginUser = JSON.parseObject(loginUserJson, LoginUser.class);

        return new UsernamePasswordAuthenticationToken(
                loginUser,
                null, // 凭证置空
                loginUser.getAuthorities()
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

    /*
     * 从传入的token中解析出相应内容，封装新的 Authentication 对象存入SpringSecurity，并返回新的 AccessToken
     * @param tokenType token类型：1-AccessToken，2-RefreshToken
     * @param token
     * @return java.lang.String
     * @author admin
     * @date 2025/05/21 09:59
     */
    public String saveNewAuthenticationAndReturnAccessToken(byte tokenType,String token) {
        Authentication authentication = null;
        if(tokenType == 1){
            // 从token中解析出Authentication
            // Authentication：SpringSecurity的认证信息对象
            // 将认证信息存储到线程绑定的SecurityContext中
            // Spring Security后续通过SecurityContextHolder获取当前用户身份，用于：鉴权（如@PreAuthorize("hasRole('ADMIN')")），获取用户信息（如@AuthenticationPrincipal LoginUser loginUser）
            authentication = getAuthenticationByAccessToken(token);
        }else if(tokenType == 2){
            authentication = getAuthenticationByRefreshToken(token);
        }else{
            throw new RuntimeException("参数错误");
        }

        //从原Token中解析出原LoginUser
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //重新设置过期时间
        Long accessTokenExpireTime = System.currentTimeMillis() + tokenProperties.getExpireTime() * 1000L;
        loginUser.setExpireTime(accessTokenExpireTime / 1000);

        // 生成新的双Token
        String newAccessToken = createAccessToken(loginUser,accessTokenExpireTime);
        //refreshToken不重新生成，只要refreshToken过期，强制重新登陆一次
        //String newRefreshToken = jwtUtils.createRefreshToken(userDetails);

        //重新创建一个新的Authentication 对象（保留原始凭证和权限），存入SpringSecurity
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                loginUser,               // 新的 Principal
                authentication.getCredentials(), // 原始凭证（如密码）
                authentication.getAuthorities() // 原始权限
        );
        //将新的Authentication存入SpringSecurity
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return newAccessToken;
    }

    public static void main(String[] args) {
        // 生成 64 字节（512 位）的 HS512 密钥
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        // 转换为 Base64 字符串存储
        String base64Key = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("安全密钥: " + base64Key);
    }
}