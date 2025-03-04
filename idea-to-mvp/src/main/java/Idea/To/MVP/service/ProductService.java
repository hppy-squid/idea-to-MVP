package Idea.To.MVP.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class ProductService {

            @Value("${stripe.secret.key}")
        private String secretKey;

        public Map<String, String> createCheckoutSession() {
                Stripe.apiKey = secretKey;

                try {

                        SessionCreateParams params = SessionCreateParams.builder()
                                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                                        .setMode(SessionCreateParams.Mode.PAYMENT)
                                        .setSuccessUrl("http://localhost:8080/success.html")
                                        .setCancelUrl("http://localhost:8080/cancel.html")
                                        .addLineItem(
                                                        SessionCreateParams.LineItem.builder()
                                                                        .setQuantity(1L)
                                                                        .setPriceData(
                                                                                        SessionCreateParams.LineItem.PriceData
                                                                                                        .builder()
                                                                                                        .setCurrency("sek")
                                                                                                        .setUnitAmount(5000L) // 50.00
                                                                                                        .setProductData(
                                                                                                                        SessionCreateParams.LineItem.PriceData.ProductData
                                                                                                                                        .builder()
                                                                                                                                        .setName("Testprodukten")
                                                                                                                                        .build())
                                                                                                        .build())
                                                                        .build())
                                        .build();

                        // Skapa sessionen hos Stripe, sedan returnera resultat
                        Session session = Session.create(params);
                        Map<String, String> map = new HashMap<>();
                        map.put("sessionId", session.getId());
                        return map;

                } catch (Exception e) {
                        throw new RuntimeException("Stripe Checkout Session error" + e.getMessage());
                }
        }
}
