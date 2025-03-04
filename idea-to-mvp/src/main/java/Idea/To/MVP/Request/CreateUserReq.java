package Idea.To.MVP.Request;

import lombok.Data;

@Data
public class CreateUserReq {
    private String username;
    private String email;
    private String password;
}
