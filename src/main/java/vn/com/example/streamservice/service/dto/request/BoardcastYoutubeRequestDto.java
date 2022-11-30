package vn.com.example.streamservice.service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.services.youtube.model.LiveBroadcastContentDetails;
import com.google.api.services.youtube.model.LiveBroadcastStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.example.streamservice.service.dto.SnippetBoardcastDto;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardcastYoutubeRequestDto {

    @JsonProperty("contentDetails")
    private LiveBroadcastContentDetails contentDetails;
    
    @JsonProperty("snippet")
    private SnippetBoardcastDto snippet;
    
    @JsonProperty("status")
    private LiveBroadcastStatus status;

}
