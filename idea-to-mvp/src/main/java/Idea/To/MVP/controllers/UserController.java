package Idea.To.MVP.controllers;

import Idea.To.MVP.DTO.UserDto;
import Idea.To.MVP.Exceptions.UserAlreadyExistException;
import Idea.To.MVP.Exceptions.UserNotFoundException;
import Idea.To.MVP.Request.CreateUserReq;
import Idea.To.MVP.Response.ApiResponse;
import Idea.To.MVP.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Idea.To.MVP.models.User;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class UserController {

    private final UserService userService;

      @GetMapping("/testUser")
    public ResponseEntity<User> getTestUser() {

        User user = new User();
        user.setUserName("test");

        return ResponseEntity.ok(user); 
    }

    @PostMapping("/users/add")
    public ResponseEntity<ApiResponse> createNewUser(@RequestBody CreateUserReq req) {
        try {
            User user = userService.createUser(req);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("User created successfully", true, userDto));
        } catch (UserAlreadyExistException e) {
           return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), false, null));
        }
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") UUID id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponse("User deleted successfully", true, null));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false, null));
        }
    }
}
