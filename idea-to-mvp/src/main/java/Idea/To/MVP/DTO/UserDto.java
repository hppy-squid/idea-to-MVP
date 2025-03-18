package Idea.To.MVP.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserDto {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String adress;
    private String postCode;


    private List<OrdersDto> orders;
    private CartDto cart;
}
