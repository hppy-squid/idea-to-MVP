package Idea.To.MVP.DTO;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Data
public class ProductDto {
   private UUID id;
   private String stripeId;
   private String pris;
   private String name;
   private boolean lagerstatus;
   private String bild;
   private String beskrivning;
   private String land;
}