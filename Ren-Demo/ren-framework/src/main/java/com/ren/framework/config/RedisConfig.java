package com.ren.framework.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.resource.ClientResources;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableAspectJAutoProxy  //启动AOP
@Slf4j
public class RedisConfig{

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 如果Redis链接失败，则抛出异常
     * @author ren
     * @date 2025/06/30 09:50
     */
    @PostConstruct
    public void validateRedisConnection() {
        int maxRetries = 3; // 最大重试次数
        long retryInterval = 2000; // 重试间隔(ms)
        int attempt = 0;

        log.info("正在验证Redis连接...");
        while (attempt < maxRetries) {
            try (RedisConnection connection = redisConnectionFactory.getConnection()) {
                //使用Ping命令进行验证（最好在Redis中设置超时时间）
                String response = connection.ping();
                if (!"PONG".equals(response)) {
                    log.error("Redis连接异常: 收到异常响应 {}", response);
                    throw new IllegalStateException("Redis连接异常: " + response);
                }
                log.info("✅ Redis连接成功");
                return; // 成功则退出方法
            } catch (Exception e) {
                attempt++;
                log.warn("Redis连接失败(尝试 {}/{}): {}", attempt, maxRetries, e.getMessage());

                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(retryInterval);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw new IllegalStateException("Redis连接失败, 超过最大重试次数", e);
                }
            }
        }
    }


    /*
     * @description  redisTemplate配置
     * @param lettuceConnectionFactory
     * @return org.springframework.data.redis.core.RedisTemplate<java.lang.String,java.lang.Object>
     * @author ren
     * @date 2025/04/17 21:27
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //使用FastJson2来序列化和反序列化redis的value值
        redisTemplate.setValueSerializer(serializer);
        //Hash的key也采用StringRedisSerializer的序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //Hash的Value也采用FastJson2的序列化方式
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}