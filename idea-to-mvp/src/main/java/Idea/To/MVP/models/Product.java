package Idea.To.MVP.models;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String stripeId;
    private String name;
    private BigDecimal price;
    private boolean inStock;
    private String image;
    private String description;
    private String originCountry;
    private String roastLevel;

}
