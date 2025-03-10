package Idea.To.MVP.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addProductToCart(CartItem item) {
        this.cartItems.add(item);
        item.setCart(this);
        updateTotalPrice();

    }

    public void deleteItem(CartItem item) {
        this.cartItems.remove(item);
        item.setCart(null);
        updateTotalPrice();
    }

    public void clearCart() {
        this.cartItems.clear();
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        this.totalPrice = this.cartItems.stream()
                .map(item -> {
                    BigDecimal unitPrice = item.getUnitPrice();

                    if (unitPrice == null) {
                        return BigDecimal.ZERO;
                    }
                    return unitPrice.multiply(BigDecimal.valueOf(item.getAmount()));
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}