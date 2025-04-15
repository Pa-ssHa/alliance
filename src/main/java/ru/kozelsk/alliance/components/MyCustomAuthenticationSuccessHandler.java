package ru.kozelsk.alliance.components;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

import java.io.IOException;

@Slf4j
@Component
public class MyCustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("onAuthenticationSuccess os starting");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = new User();
        user.setEmail(email);
        user.setName(oAuth2User.getAttribute("name"));
        user.setPassword("google");

        myUserDetailsService.registerUser(user);
        response.sendRedirect("/realty");
    }
}
