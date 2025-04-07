package ru.kozelsk.alliance.utils.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.users.Role;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

import java.security.Principal;

@Slf4j
@Service("customSecurity")
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

    public boolean checkAdmin(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();

        log.info("checkAdmin2: principal != null");
        String email = null;

        if (principal instanceof OAuth2AuthenticationToken) {
            log.info("Principal2 is OAuth2AuthenticationToken");
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) principal).getPrincipal();
            email = oauth2User.getAttribute("email");
        }
        else if (principal instanceof OAuth2User) {
            log.info("Principal2 is Oauth2User");
            email = ((OAuth2User) principal).getAttribute("email");
        }
        else if(principal instanceof UserDetails) {
            log.info("Principal2 is UserDetails");
            email = ((UserDetails) principal).getUsername();
        }
        else if (principal instanceof String) {
            log.info("Principal2 is String");
            email = (String) principal;
        }
        if(email==null){
            log.warn("Email is null for principal2: {}", principal);
            return false;
        }

        log.info("Checking admin2 rights for email: {}", email);
        return myUserDetailsService.findByEmail(email)
                .map(user -> {
                    log.info("User roles2: {}", user.getRoles());
                    return user.getRoles().contains(Role.ADMIN);
                })
                .orElse(false);
    }
}
