package Idea.To.MVP.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Idea.To.MVP.models.Product;

@RestController
@RequestMapping("/api/v1/")
public class ProductController {
    
    @GetMapping("/testProduct")
    public ResponseEntity<Product> getTestProduct() {

        Product product = new Product();
        product.setName("test");

        return ResponseEntity.ok(product); 
    }
    
}
