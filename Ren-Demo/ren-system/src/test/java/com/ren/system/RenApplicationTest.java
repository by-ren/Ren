package com.ren.system;

import com.ren.system.properties.TokenProperties;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RenApplicationTest {

    @Resource
    private TokenProperties tokenProperties;

    @Test
    public void test() {
        System.out.println("expireTime = " + tokenProperties.getExpireTime());
        System.out.println("secret = " + tokenProperties.getSecret());
        System.out.println("refreshTime = " + tokenProperties.getRefreshTime());
        System.out.println("refreshExpireTime = " + tokenProperties.getRefreshExpireTime());
        System.out.println("refreshSecret = " + tokenProperties.getRefreshSecret());
    }

}
