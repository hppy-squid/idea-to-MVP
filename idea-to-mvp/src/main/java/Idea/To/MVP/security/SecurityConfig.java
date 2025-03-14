package Idea.To.MVP.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/loggedInUser").permitAll()
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginProcessingUrl("/api/v1/auth/login")
                        .successHandler(successHandler)
                        .failureHandler((request, response, exception) -> {
                            System.out.println("Login failed: " + exception.getMessage());
                            exception.printStackTrace();

                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
                            response.setHeader("Access-Control-Allow-Credentials", "true");
                            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
                            response.setHeader("Access-Control-Allow-Headers", "Content-Type");
                            response.getWriter().write("{\"message\": \"Login failed: " + exception.getMessage() + "\", \"success\": false}");
                        })
                        .permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
