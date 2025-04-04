package ru.kozelsk.alliance.utils.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.users.Role;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

import java.security.Principal;

@Slf4j
@Service
public class CheckAuthorizeService {

    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public CheckAuthorizeService(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    public boolean checkAdmin(Principal principal) {

        log.info("checkAdmin is starting, principal: {}", principal);

        if(principal==null){
            return false;
        }

        log.info("checkAdmin: principal != null");
        String email = null;

        // Для OAuth2 аутентификации
        if (principal instanceof OAuth2AuthenticationToken) {
            log.info("Principal is OAuth2AuthenticationToken");
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) principal).getPrincipal();
            email = oauth2User.getAttribute("email");
        }
        // Для обычной аутентификации
        else {
            log.info("Principal is regular authentication");
            email = principal.getName();
        }

        if (email == null) {
            log.warn("Email is null for principal: {}", principal);
            return false;
        }

        log.info("Checking admin rights for email: {}", email);
        return myUserDetailsService.findByEmail(email)
                .map(user -> {
                    log.info("User roles: {}", user.getRoles());
                    return user.getRoles().contains(Role.ADMIN);
                })
                .orElse(false);
    }
}
