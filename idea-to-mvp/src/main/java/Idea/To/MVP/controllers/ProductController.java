package Idea.To.MVP.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Idea.To.MVP.models.Product;
import Idea.To.MVP.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/test-checkout")
    public Map<String, String> createCheckoutSession() {

        return productService.createCheckoutSession();
    }

    @GetMapping("/testProduct")
    public ResponseEntity<Product> getTestProduct() {

        Product product = new Product();
        product.setName("test");

        return ResponseEntity.ok(product);
    }

}
