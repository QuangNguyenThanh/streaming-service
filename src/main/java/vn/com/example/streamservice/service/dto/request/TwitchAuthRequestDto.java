package vn.com.example.streamservice.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwitchAuthRequestDto {

    private String authenticationChannel;

    private String redirectURI;
}
