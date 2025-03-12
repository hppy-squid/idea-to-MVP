package Idea.To.MVP.controllers;

import java.io.IOException;
import java.util.UUID;

import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/checkout/success")
    public void handleSuccess(@RequestParam("session_id") String sessionId, HttpServletResponse response) throws StripeException, IOException {
        String receiptUrl = stripeCheckoutService.getReceiptUrl(sessionId);
        response.sendRedirect(receiptUrl);
    }
}
