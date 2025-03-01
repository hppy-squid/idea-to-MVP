package Idea.To.MVP.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String userName;
    private String password;
    private String email;

    private List<OrdersDto> orders;
    private CartDto cart;
}
