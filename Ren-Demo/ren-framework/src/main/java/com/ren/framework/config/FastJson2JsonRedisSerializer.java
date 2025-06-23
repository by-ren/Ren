package com.ren.framework.config;

import java.nio.charset.Charset;

import com.ren.common.domain.constant.Constants;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;

/**
 * Redis使用FastJson序列化
 * 
 * @author ren
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T>
{
    //配置字符集
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    //配置可解析的白名单目录
    static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter(Constants.JSON_WHITELIST_STR);

    private Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz)
    {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }

        // 序列化时按照指定格式机型序列化，更好看
        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length <= 0)
        {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        //AUTO_TYPE_FILTER：JSONReader.autoTypeFilter(new String[]{}) 有两层含义
        //第一层：设置允许反序列化白名单，只有在白名单内的对象才允许被反序列化
        //第二曾：开启自动类型，开启自动类型后可以通过对象.getClass()获取对象类型
        return JSON.parseObject(str, clazz, AUTO_TYPE_FILTER);
    }
}
