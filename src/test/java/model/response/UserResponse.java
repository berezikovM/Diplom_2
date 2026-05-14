package model.response;

import lombok.Data;

@Data
public class UserResponse extends BaseResponse {
    private User user;
}