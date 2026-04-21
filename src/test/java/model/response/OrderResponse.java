package model.response;

import lombok.Data;

@Data
public class OrderResponse extends BaseResponse {
    private String name;
    private Order order;
}