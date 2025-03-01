package Idea.To.MVP.DTO;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class OrdersDto {
    private UUID orderId;
    private UUID userId;
    private Date orderDate;
    private Long totalAmount;
    private String orderStatus;

    private List<OrderItemDto> orderItems;
}
