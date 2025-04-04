package ru.kozelsk.alliance.controllers.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/reset-password")
public class ResetPasswordController {

    private final MyUserDetailsService myUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ResetPasswordController(MyUserDetailsService myUserDetailsService, PasswordEncoder passwordEncoder) {
        this.myUserDetailsService = myUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String resetPassword() {
        return "users/reset-password";
    }

    @PostMapping
    public String newPassword(@RequestParam String email,@RequestParam String name,@RequestParam String newPassword,
                              @RequestParam String confirmPassword, Model model) {
        log.info("The reset password is starting");

        if(!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            log.info("The reset password is wrong password did not match");
            return "users/reset-password";
        }

        Optional<User> user = myUserDetailsService.findByEmail(email);
        if(user.isEmpty() || !user.get().getName().equalsIgnoreCase(name.trim())) {
            model.addAttribute("error", "Неверный email или имя");
            log.info("The reset password is wrong email or name is not correct");
            return "users/reset-password";
        }

        if (newPassword.length() < 6) {
            model.addAttribute("error", "Пароль должен минимум 6 символов)");
            log.info("The reset password is wrong password is less 6 ");
            return "users/reset-password";
        }

        log.info("The reset password is ok");

        user.get().setPassword(passwordEncoder.encode(newPassword));
        myUserDetailsService.save(user.get());

        log.info("The reset password is ok and user was saved");

        return "redirect:/login?passwordReset";
    }

}
