package vn.com.example.streamservice.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import vn.com.example.streamservice.config.StreamProperties;
import vn.com.example.streamservice.entity.User;
import vn.com.example.streamservice.security.dto.JwtTokenDto;

@Component
public class JwtProvider {
    private static final String USER_ID = "userId";

    private JwtParser jwtParser;

    private final Key key;
    private final long tokenValidityInMiliseconds;

    public JwtProvider(StreamProperties streamProperties) {
        this.tokenValidityInMiliseconds = streamProperties.getJwt()
            .getTokenValidityInSeconds() * 1000;
        String secret = streamProperties.getJwt().getBase64Secret();
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }


    public JwtTokenDto createToken(User user) {
        String tokenId = UUID.randomUUID().toString();
        Date iat = new Date(System.currentTimeMillis());
        Date exp = new Date(System.currentTimeMillis() + tokenValidityInMiliseconds);
        String token = Jwts.builder()
            .setSubject(user.getEmail())
            .setIssuedAt(iat)
            .setExpiration(exp)
            .claim(USER_ID, user.getId())
            .setId(tokenId)
            .signWith(key)
            .compact();

        return JwtTokenDto
            .builder()
            .tokenId(tokenId)
            .token(token)
            .email(user.getEmail())
            .userId(user.getId())
            .expiredTime(exp)
            .build();
    }
    
    public Optional<JwtTokenDto> parseToken(String token) {
        return parseClaimsJwt(token)
            .map(claims -> JwtTokenDto
                .builder()
                .tokenId(claims.getId())
                .userId(claims.get(USER_ID, Long.class))
                .email(claims.getSubject())
                .build()
            );
    }
    private Optional<Claims> parseClaimsJwt(String token) {
        try {
            return Optional.ofNullable(jwtParser.parseClaimsJws(token).getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}