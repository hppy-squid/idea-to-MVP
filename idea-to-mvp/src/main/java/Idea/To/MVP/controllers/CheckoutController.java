package Idea.To.MVP.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Idea.To.MVP.service.StripeCheckoutService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CheckoutController {
    
    private final StripeCheckoutService stripeCheckoutService;
    
    @PostMapping("/checkout/{userId}") 
        public ResponseEntity <String> checkout(@PathVariable UUID userId) {

        try {
            String checkoutUrl = stripeCheckoutService.createCheckoutSession(userId);
            return ResponseEntity.ok(checkoutUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
