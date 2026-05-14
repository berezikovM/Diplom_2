package model.response;

import lombok.Data;

@Data
public class AuthResponse extends BaseResponse {
    private String accessToken;
    private String refreshToken;
    private User user;
}