package Idea.To.MVP.request;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Size(max = 20)
    private String firstName;
    @Size(max = 40)
    private String lastName;

    private String adress;

    private String postCode;

    @Email(message = "Please provide a valid email")
    private String email;

    @Size(min = 8, max = 100, message = "Password must be at least 8 characters long")
    private String password;
}
