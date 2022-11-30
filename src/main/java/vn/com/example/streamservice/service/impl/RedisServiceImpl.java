package vn.com.example.streamservice.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.com.example.streamservice.service.RedisService;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setData(String key, Object value, long timeLifeInSeconds) {
        redisTemplate.opsForValue().set(key, value, timeLifeInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public <T> T getData(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;
        return clazz.cast(value);
    }
}
