package Idea.To.MVP.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Idea.To.MVP.models.Product;
import Idea.To.MVP.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/")
public class ProductController {

    @Autowired
    ProductService productService;

    // @PostMapping("/test-checkout")
    // public Map<String, String> createCheckoutSession() {

    //     return productService.createCheckoutSession();
    // }

    // @GetMapping("/testProduct")
    // public ResponseEntity<Product> getTestProduct() {

    //     Product product = new Product();
    //     product.setName("test");

    //     return ResponseEntity.ok(product);
    // }

    @PostMapping("/product")
    public void addProduct(@RequestBody Product product) {
        productService.addProduct(product);
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/product") 
    public Optional<Product> getProductById(@RequestParam("id") UUID id) {
       return productService.getProductById(id);
    }

    @DeleteMapping("/product")
    public String deleteProductById(@RequestParam("id") UUID id) {
        return productService.deleteProductById(id);
    }

    // @PatchMapping("/product/{id}")
    // public void patchProductById(@PathVariable("id") UUID id) {
    //     productService.patchProductById();
    // }
}
