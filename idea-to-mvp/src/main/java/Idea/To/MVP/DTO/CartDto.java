package Idea.To.MVP.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
    private UUID cartId;
    private BigDecimal totalPrice;
    private List<CartItemDto> cartItems;
}
