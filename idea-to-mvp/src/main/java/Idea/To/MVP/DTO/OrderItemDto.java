package Idea.To.MVP.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemDto {
    private UUID id;
    private Long antal;
    private BigDecimal price;
}
