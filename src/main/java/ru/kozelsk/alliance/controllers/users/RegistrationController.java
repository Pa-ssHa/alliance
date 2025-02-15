package ru.kozelsk.alliance.controllers.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.users.Role;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping()
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final MyUserDetailsService myUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public RegistrationController(MyUserDetailsService myUserDetailsService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.myUserDetailsService = myUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // регистрация
/*
    @GetMapping("/registration")
    public String registration() {
        return "users/registration";
    }
*/

    @GetMapping("/registration")
    public String registration(@RequestParam(required = false) String phone, Model model) {
        if (phone != null) {
            model.addAttribute("phone", phone);
        }
        return "users/registration";
    }

    @PostMapping("/registration")
    public String registerUser(@RequestParam String username, @RequestParam String phone,
                               @RequestParam String password) {

        Optional<User> existingUser = myUserDetailsService.findByPhone(phone);
        if (existingUser.isPresent()) {
            return "redirect:/registration?error=phone_exists";
        }

        if (myUserDetailsService.findByPhone(phone).isPresent()) {
            return "redirect:/registration?error=phone_exists";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPhone(phone);
        newUser.setPassword(password);

        if (newUser.getUsername().equals("admin") && newUser.getPassword().equals("AdminAlliance89107091769")) {
            newUser.setRoles(Set.of(Role.ROLE_ADMIN));
        } else {
            newUser.setRoles(Set.of(Role.ROLE_USER));
        }

        newUser.setActive(true);
        newUser.setPhoneVerified(true);

        myUserDetailsService.register(newUser);

        return "redirect:/login";
    }


    @GetMapping("/login")
    public String login() {
        return "users/login";
    }


    // post запрос в контроллере AuthLoginController
/*
    @PostMapping("/login")
    public String login(@RequestParam String phone, @RequestParam String password, Model model) {

        Optional<User> userOptional = myUserDetailsService.findByPhone(phone);

        System.out.println(userOptional);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.isPhoneVerified()) {
                System.out.println(user);
                try {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(phone, password)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    return "redirect:/realty";
                } catch (Exception e) {
                    model.addAttribute("error", "Неверный пароль");
                    return "users/login";
                }
            } else {
                model.addAttribute("error", "номер не подтвержден");
                return "users/login";
            }
        }else {
            model.addAttribute("error", " пользователь с таким номером телефона уже есть");
            return "users/login";
        }
    }
*/


    // тест
/*
    @GetMapping("/test-password")
    @ResponseBody
    public String testPassword() {
        String rawPassword = "AdminAlliance89107091769"; // ваш пароль
        String encodedPassword = "$2a$10$HFFJS1t5zrJEUbl6La2CxOir.yQxFXjCKibk8TEYCBtWburSmwTsu"; // хэш пароля из базы

        if (passwordEncoder.matches(rawPassword, encodedPassword)) {
            return "Пароль совпадает";
        } else {
            return "Пароль не совпадает";
        }
    }
*/
}




