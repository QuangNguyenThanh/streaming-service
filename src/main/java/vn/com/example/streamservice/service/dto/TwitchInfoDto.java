package vn.com.example.streamservice.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwitchInfoDto {

    @JsonProperty("id")
    private Long userId;
    
    @JsonProperty("display_name")
    private String displayName;
    
    @JsonProperty("profile_image_url")
    private String imageUrl;
    
}
