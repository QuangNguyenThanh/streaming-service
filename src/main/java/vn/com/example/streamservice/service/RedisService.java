package vn.com.example.streamservice.service;

public interface RedisService {
    void setData(String key, Object value, long timeLifeInSeconds);
    <T> T getData(String key, Class<T> clazz);
}
