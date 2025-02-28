package Idea.To.MVP.Repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import Idea.To.MVP.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    
}
