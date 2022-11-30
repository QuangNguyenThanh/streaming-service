package vn.com.example.streamservice.service;

public interface TwitchStreamService {

    String oAuthTwitch(String code, String state);
}
