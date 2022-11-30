package vn.com.example.streamservice.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwitchObjectStreamKeyDto {
    
    @JsonProperty("data")
    private List<TwitchStreamKeyDto> data;
}
