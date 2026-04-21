package model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {
    private String email;
    private String name;
    // password можно добавить, если потребуется
}
