package vn.com.example.streamservice.service.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenYoutubeRequestDto {

    @NotBlank(message = "Email is required")
    private String accessToken;

}
