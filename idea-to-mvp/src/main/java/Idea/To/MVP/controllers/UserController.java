package Idea.To.MVP.controllers;

import Idea.To.MVP.DTO.UserDto;
import Idea.To.MVP.Exceptions.UserAlreadyExistException;
import Idea.To.MVP.Exceptions.UserNotFoundException;
import Idea.To.MVP.request.CreateUserReq;
import Idea.To.MVP.Response.ApiResponse;
import Idea.To.MVP.service.StripeUserService;
import Idea.To.MVP.request.UpdateUserRequest;
import Idea.To.MVP.service.UserService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Idea.To.MVP.models.User;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class UserController {

    private final UserService userService;
    private final StripeUserService stripeUserService;

    @GetMapping("/testUser")
    public ResponseEntity<User> getTestUser() {

        User user = new User();
        user.setFirstName("test");

        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/find")
    public ResponseEntity<ApiResponse> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<UserDto> userDtos = users.stream()
                    .map(userService::convertToDto)
                    .toList();
            return ResponseEntity.ok(new ApiResponse("Users found successfully", true, userDtos));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false, null));
        }
    }

    @GetMapping("/users/find/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("id") UUID id) {
        try {
            User user = userService.getUserById(id);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("User found successfully", true, userDto));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false, null));
        }
    }

    @PostMapping("/users/add")
    public ResponseEntity<ApiResponse> createNewUser(@Valid @RequestBody CreateUserReq req) {
        try {
            User user = userService.createUser(req);
            UserDto userDto = userService.convertToDto(user);
            stripeUserService.syncUsersToStripe();
            return ResponseEntity.ok(new ApiResponse("User created successfully", true, userDto));
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), false, null));
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), false, null));
        }
    }

    @PutMapping("/users/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable UUID userId, @Valid @RequestBody UpdateUserRequest req) {
        try {

            userService.deleteUser(id);
            stripeUserService.syncUsersToStripe();

            User updatedUser = userService.updateUser(userId, req);
            UserDto userDto = userService.convertToDto(updatedUser);
            return ResponseEntity.ok(new ApiResponse("User updated successfully", true, userDto));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false, null));
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), false, null));
        }catch (ConstraintViolationException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), false, null));

        }
    }

    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID userId) {
        try {
            userService.deleteUser(userId);

            return ResponseEntity.ok(new ApiResponse("User deleted successfully", true, null));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false, null));
        }
    }
}

