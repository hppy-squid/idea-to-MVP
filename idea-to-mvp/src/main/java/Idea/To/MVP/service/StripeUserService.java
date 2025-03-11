package Idea.To.MVP.service;

import java.util.ArrayList;
import java.util.List;

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
    List<String> stripeProductIds = new ArrayList<>();

    @Value("${stripe.secret.key}")
    private String secretKey;
    

    @PostConstruct
    public void uploadUsersToStripe() {

        Stripe.apiKey = secretKey;
        List<User> users = userRepository.findAll();
        for (User user : users) {

            if (user.getStripeId() == null || user.getStripeId().isEmpty()) {
                try {
                    CustomerCreateParams params = CustomerCreateParams.builder()
                            .setName(user.getFirstName() + " " + user.getLastName())
                            .setEmail(user.getEmail())
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
    

