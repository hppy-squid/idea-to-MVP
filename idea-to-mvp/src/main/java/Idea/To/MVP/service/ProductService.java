package Idea.To.MVP.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Idea.To.MVP.Repository.ProductRepository;
import Idea.To.MVP.models.Product;
import lombok.AllArgsConstructor;

@Transactional
@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StripeProductService stripeProductService;

    // Skapar en ny produkt och synkroniserar med Stripe
    public void addProduct(Product product) {

                productRepository.save(product);
                stripeProductService.syncProductsToStripe();
                }

    // Hämtar alla produkter
    public List<Product> getAllProducts() {
               return productRepository.findAll();
        }
    // Hämtar en produkt baserat på ID
    public Optional<Product> getProductById(UUID id) {
            return productRepository.findById(id);
    }

    // Tar bort en produkt baserat på id
    public String deleteProductById(UUID id) {
            if(getProductById(id) != null) {
                    productRepository.deleteById(id);
                    stripeProductService.syncProductsToStripe();
                    return id + " Borttaget";
            }

            return "Id finns inte";
    }


    // Uppdaterar en produkt baserat på ID och synkroniserar med Stripe
    public Optional<Product> patchProductById(UUID id, Product product) {
            Optional<Product> updatedProduct = productRepository.findById(id)
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
                    if (product.getRoastLevel() != null) {
                            existingProduct.setRoastLevel(product.getRoastLevel());
                        }
                    return productRepository.save(existingProduct);
                });
            // om uppdatering lyckas så synkronisera med Stripe
            updatedProduct.ifPresent(updated -> stripeProductService.syncProductsToStripe());
            return updatedProduct;
        }

}
