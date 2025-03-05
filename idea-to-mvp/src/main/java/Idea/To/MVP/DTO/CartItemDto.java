package Idea.To.MVP.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CartItemDto {
    private UUID cartItemId;
    private Long amount;
    private Long unitPrice;
    private BigDecimal totalPrice;

    private ProductDto product;
}
