package ru.kozelsk.alliance.utils.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

import java.security.Principal;

@Slf4j
@Service
public class UserFromPrincipal {

    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public UserFromPrincipal(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }


    public User getUser(Principal principal) {

        if(principal==null){
            return null;
        }

        String email = null;
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) principal).getPrincipal();
            email = oauth2User.getAttribute("email");
        }
        // Для обычной аутентификации
        else {
            email = principal.getName();
        }
        if(email==null){
            return null;
        }
        return myUserDetailsService.findByEmail(email).get();
    }

    public Integer getUserId(Principal principal) {

        if(principal==null){
            return null;
        }

        String email = null;
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) principal).getPrincipal();
            email = oauth2User.getAttribute("email");
        }
        // Для обычной аутентификации
        else {
            email = principal.getName();
        }
        if(email==null){
            return null;
        }
        return myUserDetailsService.findByEmail(email).get().getId();
    }
}
