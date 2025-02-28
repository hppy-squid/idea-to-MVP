package Idea.To.MVP.Repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import Idea.To.MVP.models.Cart;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    
}