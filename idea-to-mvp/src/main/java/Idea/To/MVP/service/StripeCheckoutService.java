package Idea.To.MVP.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import Idea.To.MVP.Repository.CartRepository;
import Idea.To.MVP.Repository.UserRepository;
import Idea.To.MVP.models.Cart;
import Idea.To.MVP.models.CartItem;
import Idea.To.MVP.models.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StripeCheckoutService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public String createCheckoutSession(UUID userId) throws StripeException {
        Stripe.apiKey = secretKey;

        // Hämta användaren och korgen
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = user.getCart();
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Skapa LineItems-lista för Stripe Checkout
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {
            // Hämta produkten från Stripe
            Product stripeProduct = Product.retrieve(item.getProduct().getStripeId());

            // Hämta `default_price` som skapades via `uploadProductsToStripe()`
            String priceId = stripeProduct.getDefaultPrice();
            if (priceId == null) {
                throw new RuntimeException("No default price found for product: " + item.getProduct().getName());
            }

            // Skapa Stripe LineItem
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setPrice(priceId)
                    .setQuantity(item.getAmount())
                    .build();

            lineItems.add(lineItem);
        }

        // Skapa Stripe Checkout-session
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addAllLineItem(lineItems)
                .setCustomer(user.getStripeId())
                .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                .setSuccessUrl("http://localhost:8080/api/v1/checkout/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://127.0.0.1:5500/varukorg.html")
                .build();

        Session session = Session.create(params);

        // Spara Stripe-session-ID i cart om du vill
        cart.setStripeSessionId(session.getId());
        cartRepository.save(cart);

        return session.getUrl(); // Returnera URL till Stripe Checkout
    }

    public String getReceiptUrl(String sessionId) throws StripeException {
        Stripe.apiKey = secretKey;

        Session session = Session.retrieve(sessionId);
        String paymentIntentId = session.getPaymentIntent();
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        Charge charge = Charge.retrieve(paymentIntent.getLatestCharge());

        return charge.getReceiptUrl();
    }

}
