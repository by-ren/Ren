
package com.ren.common.manager.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Redis监控信息工具类（由于不是纯静态方法，参与了业务逻辑的计算，所以设计为Manager模块，而不是Utils模块）
 * 
 * @author ren
 */
@Component
public class RedisMonitorInfoManager {

    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 获取Redis服务器基本信息
     * @author ren
     * @date 2025/06/17 14:33
     */
    public Properties getRedisBasicInfo(){
        //execute执行因为不常用而未经过封装的Redis命令
        return (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
    }

    /**
     * 获取Redis命令统计信息
     * @return java.util.Properties
     * @author ren
     * @date 2025/06/17 14:34
     */
    public Properties getRedisCommandStatInfo(){
        //execute执行因为不常用而未经过封装的Redis命令
        return (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
    }

    /**
     * 获取当前数据库key的数量
     * @return java.lang.Object
     * @author ren
     * @date 2025/06/17 14:35
     */
    public Object getRedisKeyNumber(){
        //execute执行因为不常用而未经过封装的Redis命令
        return redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());
    }

}