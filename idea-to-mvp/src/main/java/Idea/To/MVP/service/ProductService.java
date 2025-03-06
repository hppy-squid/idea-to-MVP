package Idea.To.MVP.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import Idea.To.MVP.Repository.ProductRepository;
import Idea.To.MVP.models.Product;

@Transactional
@Service
public class ProductService {

        @Autowired
        ProductRepository productRepository;

        //     @Value("${stripe.secret.key}")
        // private String secretKey;

        // public Map<String, String> createCheckoutSession() {
        //         Stripe.apiKey = secretKey;

        //         try {

        //                 SessionCreateParams params = SessionCreateParams.builder()
        //                                 .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
        //                                 .setMode(SessionCreateParams.Mode.PAYMENT)
        //                                 .setSuccessUrl("http://localhost:8080/success.html")
        //                                 .setCancelUrl("http://localhost:8080/cancel.html")
        //                                 .addLineItem(
        //                                                 SessionCreateParams.LineItem.builder()
        //                                                                 .setQuantity(1L)
        //                                                                 .setPriceData(
        //                                                                                 SessionCreateParams.LineItem.PriceData
        //                                                                                                 .builder()
        //                                                                                                 .setCurrency("sek")
        //                                                                                                 .setUnitAmount(5000L) // 50.00
        //                                                                                                 .setProductData(
        //                                                                                                                 SessionCreateParams.LineItem.PriceData.ProductData
        //                                                                                                                                 .builder()
        //                                                                                                                                 .setName("Testprodukten")
        //                                                                                                                                 .build())
        //                                                                                                 .build())
        //                                                                 .build())
        //                                 .build();

        //                 // Skapa sessionen hos Stripe, sedan returnera resultat
        //                 Session session = Session.create(params);
        //                 Map<String, String> map = new HashMap<>();
        //                 map.put("sessionId", session.getId());
        //                 return map;

        //         } catch (Exception e) {
        //                 throw new RuntimeException("Stripe Checkout Session error" + e.getMessage());
        //         }
        // }

        public void addProduct(Product product) {

                productRepository.save(product);
                System.out.println("hej");
        }

        public List<Product> getAllProducts() {
               return productRepository.findAll();
        }

        public Optional<Product> getProductById(UUID id) {
                return productRepository.findById(id);
        }

        public String deleteProductById(UUID id) {
                if(getProductById(id) != null) {
                        productRepository.deleteById(id);
                        return id + " Borttaget";
                }
                
                return "Id finns inte";
        }


        public Optional<Product> patchProductById(UUID id, Product product) {
                
                      return productRepository.findById(id)
                      .map(existingProduct -> {
                        if (product.getName() != null) {
                                existingProduct.setName(product.getName());
                                }      
                        if (product.getPrice() != null) {
                                existingProduct.setPrice(product.getPrice());
                                        }
                        if (product.isInStock() != existingProduct.isInStock()) {
                                existingProduct.setInStock(product.isInStock());
                                }  
                        if (product.getDescription() != null) {
                                existingProduct.setDescription(product.getDescription());
                                                        } 
                        if (product.getOriginCountry() != null) {
                                existingProduct.setOriginCountry(product.getOriginCountry());
                                }
                        if (product.getImage() != null) {
                                existingProduct.setImage(product.getImage());
                                }                                    
                        return productRepository.save(existingProduct);
                });  
        }
}
