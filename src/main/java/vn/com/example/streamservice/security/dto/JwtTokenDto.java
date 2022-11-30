package vn.com.example.streamservice.security.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenDto {
    private Long userId;
    private String tokenId;
    private String token;
    private String email;
    private String role;
    private Date expiredTime;
}