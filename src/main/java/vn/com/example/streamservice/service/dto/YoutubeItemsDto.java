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
public class YoutubeItemsDto {
    
    @JsonProperty("snippet")
    private YoutubeSnippetDto snippet;
    
    @JsonProperty("id")
    private String channelId;
}
