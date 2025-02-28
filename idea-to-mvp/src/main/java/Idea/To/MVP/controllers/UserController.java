package Idea.To.MVP.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Idea.To.MVP.models.User;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

      @GetMapping("/testUser")
    public ResponseEntity<User> getTestUser() {

        User user = new User();
        user.setUserName("test");

        return ResponseEntity.ok(user); 
    }
}
