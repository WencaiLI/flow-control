package com.lwc.flowcontrol.commons.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: liwencai
 * @Date: 2022/7/20 15:41
 * @Description:
 */
@Service
public class RedisUtils {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @param expireTime 有效时间，单位秒
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}