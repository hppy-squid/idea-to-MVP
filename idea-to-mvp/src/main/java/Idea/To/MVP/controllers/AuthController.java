package Idea.To.MVP.controllers;

import Idea.To.MVP.DTO.UserDto;
import Idea.To.MVP.Response.ApiResponse;
import Idea.To.MVP.models.User;
import Idea.To.MVP.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "http://localhost:8080"}, allowCredentials = "true")
public class AuthController {

    //kontrollerar vilken user som är inloggad
    private final UserService userService;

    @GetMapping("/loggedInUser")
    public ResponseEntity<ApiResponse> getUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
        String userEmail = principal.getName();
        User user = userService.authenticateUser(userEmail);
        UserDto userDto = userService.convertToDto(user);
        return ResponseEntity.ok(new ApiResponse("Currently logged in user", true, userDto));
    }

}
