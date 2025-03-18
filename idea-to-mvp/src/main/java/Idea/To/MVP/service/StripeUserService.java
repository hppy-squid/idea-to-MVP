package Idea.To.MVP.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;

import Idea.To.MVP.Repository.UserRepository;
import Idea.To.MVP.models.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StripeUserService {

    private final UserRepository userRepository;

    @Value("${stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    // tar bort Stripe alla users som inte finns i databasen
    public void syncUsersToStripe() {

        Stripe.apiKey = secretKey;
        try {
            Map<String, Object> params = new HashMap<>();
            // hämtar alla Stripe users, stripe kräver en Map för detta.
            com.stripe.model.CustomerCollection stripeCustomers = Customer.list(params);

            for (Customer stripeCustomer : stripeCustomers.getData()) {
                String metadataId = stripeCustomer.getMetadata().get("id");
                // kontrollerar om metadata saknas eller om ID inte längre finns
                if (metadataId == null || !userRepository.existsById(UUID.fromString(metadataId))) {
                    stripeCustomer.delete();
                }
            }
        } catch (StripeException e) {
            e.printStackTrace();
        }
        updateUsersWithNoStripeId();
    }

    public void updateStripeCustomer(User user) throws StripeException {
        Stripe.apiKey = secretKey;

        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("email", user.getEmail());
        customerParams.put("name", user.getFirstName() + " " + user.getLastName());

        // Address information
        Map<String, Object> address = new HashMap<>();
        address.put("line1", user.getAdress());
        address.put("postal_code", user.getPostCode());
        address.put("country", "SE"); // Sweden

        customerParams.put("address", address);

        // Add metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("id", user.getId().toString());
        customerParams.put("metadata", metadata);

        Customer.retrieve(user.getStripeId()).update(customerParams);
    }


    // lägg till users utan stripeId i Stripe och lägg till ett stripeId.
    private void updateUsersWithNoStripeId() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getStripeId() == null || user.getStripeId().isEmpty()) {
                try {
                    CustomerCreateParams params = CustomerCreateParams.builder()
                            .setName(user.getFirstName() + " " + user.getLastName())
                            .setEmail(user.getEmail())
                            .setAddress(CustomerCreateParams.Address.builder()
                                    .setLine1(user.getAdress())
                                    .setPostalCode(user.getPostCode())
                                    .setCountry("SE")
                                    .build())
                            .putMetadata("id", String.valueOf(user.getId()))
                            .build();
                    Customer stripeCustomer = Customer.create(params);
                    user.setStripeId(stripeCustomer.getId());
                    userRepository.save(user);
                } catch (StripeException e) {
                    // Hantera fel, t.ex. logga problemet
                    System.err.println("Fel vid uppladdning av user: " + user.getFirstName());
                    e.printStackTrace();
                }
            }
        }
    }
}
