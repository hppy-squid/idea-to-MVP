package Idea.To.MVP.security;

import Idea.To.MVP.DTO.UserDto;
import Idea.To.MVP.Response.ApiResponse;
import Idea.To.MVP.models.User;
import Idea.To.MVP.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String email = authentication.getName();
        User user = userService.authenticateUser(email);
        UserDto userDto = userService.convertToDto(user);

        ApiResponse apiResponse = new ApiResponse(
                "Login has been successful",
                true,
                userDto
        );

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

    }
}
