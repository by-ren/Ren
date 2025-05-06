package com.ren.admin;

import com.ren.framework.properties.TokenProperties;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RenApplicationTest {

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void test() {
        System.out.println("expireTime = " + tokenProperties.getExpireTime());
        System.out.println("secret = " + tokenProperties.getSecret());
        System.out.println("refreshTime = " + tokenProperties.getRefreshTime());
        System.out.println("refreshExpireTime = " + tokenProperties.getRefreshExpireTime());
        System.out.println("refreshSecret = " + tokenProperties.getRefreshSecret());
    }

    @Test
    public void printAliases() {
        TypeAliasRegistry registry = sqlSessionFactory.getConfiguration().getTypeAliasRegistry();
        System.out.println("已注册别名: " + registry.getTypeAliases());
    }

}
