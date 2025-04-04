package ru.kozelsk.alliance.controllers.users;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

import java.security.Principal;

@RestController
@RequestMapping("/rest/api/auth")
public class RegistrationRestController {

    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public RegistrationRestController(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @GetMapping
    public ResponseEntity<String> temp(@AuthenticationPrincipal OAuth2User user){
        if (user==null){
            return ResponseEntity.status(401).body("User not found");
        }
        String role = myUserDetailsService.findByEmail(user.getAttribute("email")).get().getRoles().toString();
        return ResponseEntity.status(200).body(user.getAttribute("email") + " " + user.getAttribute("name") + " " + role);
    }

    @GetMapping("/princ")
    public ResponseEntity<String> temp(Principal principal){
        if (principal==null){
            return ResponseEntity.status(401).body("User not found");
        }

        String role = myUserDetailsService.findByEmail(principal.getName()).get().getRoles().toString();
        return ResponseEntity.status(200).body(principal.getName() + " " + role);
    }

    @PostMapping("/registration")
    public User registerUser(@RequestBody User user) {
        return myUserDetailsService.save(user);
    }

}
