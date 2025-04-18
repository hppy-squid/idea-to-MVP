package Idea.To.MVP.service;


import Idea.To.MVP.DTO.UserDto;
import Idea.To.MVP.Exceptions.UserAlreadyExistException;
import Idea.To.MVP.Exceptions.UserNotFoundException;
import Idea.To.MVP.Repository.UserRepository;
import Idea.To.MVP.request.CreateUserReq;
import Idea.To.MVP.models.User;
import Idea.To.MVP.request.UpdateUserRequest;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService{

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final StripeUserService stripeUserService;
    private final PasswordEncoder passwordEncoder;


    // Hämtar alla användare
    public List<User> getAllUsers() {
        List <User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found");
        }
        return users;
    }

    // Hämtar en användare baserat på Id
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
    }

    // Skapar en användare baserat på CreateUserReq
    public User createUser(CreateUserReq createUserReq) {
        return Optional.of(createUserReq).filter(user ->!userRepository.existsByEmail(user.getEmail()))
                .map(createUserRequest -> {
                    User user = new User();
                    user.setFirstName(createUserReq.getFirstName());
                    user.setLastName(createUserReq.getLastName());
                    user.setPassword(passwordEncoder.encode(createUserReq.getPassword()));
                    user.setEmail(createUserReq.getEmail());
                    user.setPostCode(createUserReq.getPostCode());
                    user.setAdress(createUserReq.getAdress());
                    return userRepository.save(user);
                }).orElseThrow(() -> new UserAlreadyExistException("There is already a user with this: " + createUserReq.getEmail() + " email"));
    }

    // Uppdaterar en användare baserat på UpdateUserReq
    @Transactional
    public User updateUser(UUID userId, UpdateUserRequest updateUserRequest) {
        return userRepository.findById(userId)
                .map(user -> {
                    if (updateUserRequest.getEmail() != null &&
                            !updateUserRequest.getEmail().equals(user.getEmail()) &&
                            userRepository.existsByEmail(updateUserRequest.getEmail())) {
                        throw new UserAlreadyExistException("Email already in use: " + updateUserRequest.getEmail());
                    }
                    if (updateUserRequest.getEmail() != null) {
                        user.setEmail(updateUserRequest.getEmail());
                    }
                    if (updateUserRequest.getFirstName() != null) {
                        user.setFirstName(updateUserRequest.getFirstName());
                    }
                    if (updateUserRequest.getLastName() != null) {
                        user.setLastName(updateUserRequest.getLastName());
                    }
                    if (updateUserRequest.getAdress() != null) {
                        user.setAdress(updateUserRequest.getAdress());
                    }
                    if (updateUserRequest.getPostCode() != null) {
                        user.setPostCode(updateUserRequest.getPostCode());
                    }
                    if (updateUserRequest.getPassword() != null) {
                        user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
                    }

                    User updatedUser = userRepository.save(user);

                    try {
                        stripeUserService.updateStripeCustomer(updatedUser);
                    } catch (StripeException e) {
                        System.err.println("Failed to update Stripe customer: " + e.getMessage());
                    }

                    return updatedUser;
                })
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
    }

    // TaR bort en användare
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.findById(id).ifPresentOrElse(userRepository :: delete, () -> {
            throw new UserNotFoundException("User with id: " + id + " not found");
        });
    }

    // Authentiserar en användare
    @Transactional(readOnly = true)
    public User authenticateUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (user.getOrders() != null) {
            user.getOrders().size();
        }
        if (user.getCart() != null) {
            user.getCart().getId();
        }

        return user;
    }

    // Konveraterar en användare till UserDto
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
