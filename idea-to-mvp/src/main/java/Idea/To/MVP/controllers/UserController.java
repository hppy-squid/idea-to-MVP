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

import java.util.List;
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

    @GetMapping("/users/find")
    public ResponseEntity<ApiResponse> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List <UserDto> userDtos = users.stream()
                    .map(userService :: convertToDto)
                    .toList();
            return ResponseEntity.ok(new ApiResponse("Users found successfully", true, userDtos));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false, null));
        }
    }

    @GetMapping ("/users/find/{id}")
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
