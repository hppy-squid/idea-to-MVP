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
        // Hämta användarens e-postadress från autentiseringen
        String email = authentication.getName();
        // Autentisera användaren med hjälp av e-postadressen
        User user = userService.authenticateUser(email);
        // Konvertera användaren till en UserDto
        UserDto userDto = userService.convertToDto(user);

        // Skapa ett ApiResponse-objekt för att skicka tillbaka som svar
        ApiResponse apiResponse = new ApiResponse(
                "Login has been successful",
                true,
                userDto
        );
        // Ställ in HTTP-statusen till OK (200)
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        // Ställ in CORS-rubriker
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

    }
}
