package Idea.To.MVP.Repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Idea.To.MVP.models.Cart;
public interface CartRepository extends JpaRepository<Cart, UUID> {
     @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.id = :cartId")
    Optional<Cart> findCartWithItemsById(@Param("cartId") UUID cartId);

}
