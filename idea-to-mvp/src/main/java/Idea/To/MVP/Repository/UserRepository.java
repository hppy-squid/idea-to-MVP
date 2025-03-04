package Idea.To.MVP.Repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import Idea.To.MVP.models.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
}
