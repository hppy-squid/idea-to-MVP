package Idea.To.MVP.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.ProductCreateParams;

import Idea.To.MVP.Repository.ProductRepository;
import Idea.To.MVP.models.Product;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StripeProductService {

    private final ProductRepository productRepository;
    List<String> stripeProductIds = new ArrayList<>();

    @Value("${stripe.secret.key}")
    private String secretKey;


    public void removeUnusedProductsFromStripe() {
        // Sätt API-nyckeln för Stripe
        Stripe.apiKey = secretKey;

        // Skapa parametrar, t.ex. ange ett maxantal om det behövs
        Map<String, Object> params = new HashMap<>();

        try {
            // Hämta produktlistan från Stripe
            com.stripe.model.ProductCollection productCollection = com.stripe.model.Product.list(params);

            // Iterera igenom alla produkter och radera dem
            productCollection.getData().forEach(product -> {

                if (!productRepository.findAllById(List.of(UUID.fromString(product.getMetadata().get("id")))).iterator().hasNext()) {
                    try {
                        product.delete();
                    }
                    
                    catch (StripeException e) {
                       System.err.println("Kunde inte radera produkt : " + product.getId());
                   }
                }
            });            
        } 
        catch (StripeException e) {
            System.err.println("Fel vid hämtning av Stripe produkter: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void uploadProductsToStripe() {

        Stripe.apiKey = secretKey;
        List<Product> products = productRepository.findAll();
        for (Product product : products) {

            if (product.getStripeId() == null || product.getStripeId().isEmpty()) {
                try {
                    if (product.getPrice() != null) {                     
                        long priceInSmallestUnit = product.getPrice()
                    .multiply(new BigDecimal("100"))
                    .longValueExact();
                    ProductCreateParams params = ProductCreateParams.builder()
                            .setName(product.getName())
                            .setDescription(product.getDescription())
                            .setDefaultPriceData(
                         ProductCreateParams.DefaultPriceData.builder()
                         .setCurrency("sek")        
                         .setUnitAmount(priceInSmallestUnit)
                             .build()
                            )
                    .putMetadata("id", String.valueOf(product.getId()))
                            .build();

                    com.stripe.model.Product stripeProduct = com.stripe.model.Product.create(params);
                    product.setStripeId(stripeProduct.getId());
                    productRepository.save(product);}
                } catch (StripeException e) {
                    // Hantera fel, t.ex. logga problemet
                    System.err.println("Fel vid uppladdning av produkt: " + product.getName());
                    e.printStackTrace();
                }
            }
        }
        fetchProductsFromStripe();
    }


    public void fetchProductsFromStripe() {
        // Sätt API-nyckeln
        Stripe.apiKey = secretKey;

        try {
            // Parametrar för listan, t.ex. sätt ett maxantal produkter
            Map<String, Object> params = new HashMap<>();

            // Hämta produktlistan från Stripe
            com.stripe.model.ProductCollection productCollection = com.stripe.model.Product.list(params);

            // Iterera över produkterna
            for (com.stripe.model.Product stripeProduct : productCollection.getData()) {
                // Här kan du t.ex. skriva ut produktens namn och id
                List<String> metadataValues = new ArrayList<>(stripeProduct.getMetadata().values());
                stripeProductIds.addAll(metadataValues);
                // här tar vi bort produkter efter ID:s som inte finns
                removeUnusedProductsFromStripe();

                System.out.println("Stripe produkt hittad: " + stripeProduct.getName() + " (ID: "
                        + stripeProduct.getId() + ")" + stripeProductIds);
            }
        } catch (StripeException e) {
            System.err.println("Fel vid hämtning av Stripe produkter.");
            e.printStackTrace();
        }
    }

}
