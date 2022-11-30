package vn.com.example.streamservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix="application", ignoreInvalidFields=true)
@Getter
@Setter
public class StreamProperties{
    private final Twitch twitch = new Twitch();
    private final Youtube youtube = new Youtube();
    private final JWT jwt = new JWT();
    private final CorsConfiguration cors = new CorsConfiguration();
    
    @Getter
    @Setter
    public static class Twitch {
        private TwitchUrl url;
        private TwitchOAuth2Client oauth;
    }
    
    @Getter
    @Setter
    public static class Youtube {
        private YoutubeUrl url;
    }
    
    @Getter
    @Setter
    public static class YoutubeUrl {
        private String createStream;
        private String channelInfo;
        private String callBack;
        private String createBoardcast;
        private String bind;
    }
    
    @Getter
    @Setter
    public static class TwitchUrl {
        private String gateway;
        private String authentication;
        private String userInfo;
        private String streamKey;
    }

    @Getter
    @Setter
    public static class TwitchOAuth2Client {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
    }

    @Getter
    @Setter
    public static class JWT {
        private String base64Secret;
        private long tokenValidityInSeconds;
    }
}
