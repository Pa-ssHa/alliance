package ru.kozelsk.alliance.controllers.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.users.Role;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

import java.util.Set;

@Controller
@RequestMapping()
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

    @GetMapping("/registration")
    public String registration(){
        return "users/registration";
    }

    @PostMapping("/registration")
    public String registerUser(User user){
        if(user.getUsername().equals("admin") && user.getPassword().equals("AdminAlliance89107091769")){
            user.setRoles(Set.of(Role.ADMIN));
        }else {
            user.setRoles(Set.of(Role.USER));
        }
        user.setActive(true);

        myUserDetailsService.register(user);
        return "redirect:/login";
    }



    @GetMapping("/login")
    public String login(){
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password){
        try{
            //    создаем объект аутентификации
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(name, password)
            );

            // устанавливаем аутентификацию в контекст безопасности
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/realty";
        }catch (Exception e){
            return "redirect:/login&error";
        }
    }




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


}
