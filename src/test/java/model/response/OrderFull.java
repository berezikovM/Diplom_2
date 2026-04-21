package model.response;

import lombok.Data;
import java.util.List;

@Data
public class OrderFull {
    private List<String> ingredients;
    private String _id;
    private String status;
    private Integer number;
    private String createdAt;
    private String updatedAt;
}
