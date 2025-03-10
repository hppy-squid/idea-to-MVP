package Idea.To.MVP.Repository;

import Idea.To.MVP.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    void deleteAllByCartId(UUID id);
}
