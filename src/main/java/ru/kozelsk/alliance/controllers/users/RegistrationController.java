package ru.kozelsk.alliance.controllers.users;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.users.Role;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

import java.util.Collections;

@Slf4j
@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final MyUserDetailsService userService;

    @Autowired
    public RegistrationController(MyUserDetailsService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "users/registration";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") @Valid User user,
                               BindingResult bindingResult,
                               Model model,
                               @AuthenticationPrincipal OAuth2User oauthUser) {

        // Проверка на существующий email
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "error.user", "Этот email уже используется");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "users/registration";
        }

        user.setPassword(user.getPassword());
        user.setActive(true);
        userService.registerUser(user);
        return "redirect:/login?success";

//        if (oauthUser == null) {
//            user.setPassword(user.getPassword());
//            user.setActive(true);
//            userService.registerUser(user);
//            return "redirect:/login?success";
//        } else {
//            return "redirect:/registration/api/oauth2";
//        }
    }

//    @GetMapping("/api/oauth2")
//    public String registrationWithOAuth(@AuthenticationPrincipal OAuth2User oauthUser) {
//        if (oauthUser == null) {
//            return "redirect:/realty";
//        }
//
//        log.info("Role: " + oauthUser.getAuthorities());
//
//        String email = oauthUser.getAttribute("email");
//        if (userService.findByEmail(email).isPresent()) {
//            return "redirect:/realty";
//        }
//
//        User newUser = new User();
//        newUser.setName(oauthUser.getAttribute("name"));
//        newUser.setEmail(email);
//        newUser.setPassword(passwordEncoder.encode("google"));
//
//        userService.registerUser(newUser);
//
//        return "redirect:/realty";
//    }
}
