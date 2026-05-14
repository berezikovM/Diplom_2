package model.response;

import lombok.Data;
import java.util.List;

@Data
public class OrdersListResponse extends BaseResponse {
    private List<OrderFull> orders;
    private Integer total;
    private Integer totalToday;
}
