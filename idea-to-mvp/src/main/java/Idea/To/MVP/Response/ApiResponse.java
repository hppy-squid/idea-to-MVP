package Idea.To.MVP.Response;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse {
    private String message;
    private boolean success;
    private Object data;
}
