package com.vinfast.rental_service.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinfast.rental_service.dtos.response.TokenResponse;
import com.vinfast.rental_service.model.User;
import com.vinfast.rental_service.repository.UserRepository;
import com.vinfast.rental_service.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
        String name = principal.getAttribute("name");
        String email = principal.getAttribute("email");

        assert name != null;

        User user = userRepository.findByEmail(email).orElseGet(() ->
        {
            String defaultPhone = "N/A";
            String defaultPassword = "OAUTH2";
            return userRepository.save(
                    User.builder()
                            .name(name)
                            .email(email)
                            .phone(defaultPhone)
                            .password(defaultPassword)
                            .build()
            );
        });

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        TokenResponse res = TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).userId(user.getId()).build();

        response.getWriter().write(new ObjectMapper().writeValueAsString(res));
    }
}
