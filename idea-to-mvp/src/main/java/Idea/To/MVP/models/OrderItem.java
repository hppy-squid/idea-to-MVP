package Idea.To.MVP.models;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Long amount;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderItem(Orders order, Product product, long amount, BigDecimal price) {
        this.order = order;
        this.product = product;
        this.amount = amount;
        this.price = price;
    }
}
