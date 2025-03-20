package Idea.To.MVP.models;

import java.math.BigDecimal;
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

    private String stripeSessionId;

    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    //  Lägg till en produkt i kundvagnen samt uppdaterar totalpriset
    public void addProductToCart(CartItem item) {
        this.cartItems.add(item);
        item.setCart(this);
        updateTotalPrice();

    }

    // Tar bort en produkt från kundvagnen samt och uppdaterar totalpriset
    public void deleteItem(CartItem item) {
        this.cartItems.remove(item);
        item.setCart(null);
        updateTotalPrice();
    }

    // Töm kundvagnen och uppdatera totalpriset
    public void clearCart() {
        this.cartItems.clear();
        updateTotalPrice();
    }

    // Uppdatera totalpriset baserat på produkterna i kundvagnen
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