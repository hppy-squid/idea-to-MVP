package Idea.To.MVP.controllers;

import Idea.To.MVP.DTO.UserDto;
import Idea.To.MVP.Exceptions.UserAlreadyExistException;
import Idea.To.MVP.Request.CreateUserReq;
import Idea.To.MVP.Response.ApiResponse;
import Idea.To.MVP.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Idea.To.MVP.models.User;
import static org.springframework.http.HttpStatus.CONFLICT;

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
}
