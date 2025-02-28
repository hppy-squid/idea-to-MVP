package Idea.To.MVP.models;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Date orderDate;
    private Long totalAmount;
    private String orderStatus;
    private String user;
    // private orderitems?
}
