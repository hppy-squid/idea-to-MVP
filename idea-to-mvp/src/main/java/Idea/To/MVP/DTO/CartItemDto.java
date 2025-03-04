package Idea.To.MVP.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CartItemDto {
    private UUID id;
    private Long antal;
    private Long styckPris;
    private BigDecimal totalPris;

    private ProductDto product;
}
