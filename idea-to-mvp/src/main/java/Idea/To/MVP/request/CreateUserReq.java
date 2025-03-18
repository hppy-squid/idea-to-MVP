package Idea.To.MVP.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserReq {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Adress is required")
    private String adress;

    @NotBlank(message = "Post code is required")
    private String postCode;

    @Size(min = 8, max = 100, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password is required")
    private String password;
}
