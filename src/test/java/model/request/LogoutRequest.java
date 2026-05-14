package model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogoutRequest {
    private String token;   // refreshToken
}