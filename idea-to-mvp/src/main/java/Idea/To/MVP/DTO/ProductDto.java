package Idea.To.MVP.DTO;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Data
public class ProductDto {
   private UUID productId;
   private String stripeId;
   private String price;
   private String productName;
   private boolean inStock;
   private String image;
   private String description;
   private String originCountry;
}