package ru.kozelsk.alliance.controllers.users;

import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpSession;
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
import ru.kozelsk.alliance.services.users.FirebaseService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;
import ru.kozelsk.alliance.services.users.SmsService;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
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

    // регистрация
    @GetMapping("/registration")
    public String registration() {
        return "users/registration";
    }


    @PostMapping("/registration")
    public String registerUser(@RequestParam String username, @RequestParam String phone,
                               @RequestParam String password) {

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


    @PostMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password) {
        User user = myUserDetailsService.findByUsername(name).orElse(null);
        if (user == null) {
            user = myUserDetailsService.findByPhone(name).orElse(null);
        }
        if (user != null && user.isPhoneVerified()) {
            // создаем объект аутентификации
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(name, password)
            );

            // устанавливаем аутентификацию в контекст безопасности
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/realty";
        } else {
            return "redirect:/login&error";
        }
    }



    // тест
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




//    @GetMapping("/verify-phone")
//    public String verifyPhone() {
//        return "users/verify-phone";
//    }
//
//    // подтверждение
//    @PostMapping("/verify-phone")
//    public String verifyPhone(@RequestParam String code, HttpSession session) {
//
//        // получаем пользователя из сессии
//        User tempUser = (User) session.getAttribute("tempUser");
//
//        if (tempUser != null) {
//
//
//            if (tempUser.getUsername().equals("admin") && tempUser.getPassword().equals("AdminAlliance89107091769")) {
//                tempUser.setRoles(Set.of(Role.ROLE_ADMIN));
//            } else {
//                tempUser.setRoles(Set.of(Role.ROLE_USER));
//            }
//            tempUser.setActive(true);
//            tempUser.setPhoneVerified(true);
//            myUserDetailsService.register(tempUser);
//
//            // очищаем временного пользователя из сессии
//            session.removeAttribute("tempUser");
//
//            return "redirect:/realty";
//        } else {
//            return "redirect:/verify-phone?error";
//        }
//
//    }




//    @PostMapping("/registration")
//    public String registerUser(User user) {
//
//        // генерация кода
//        String verificationCode = smsService.generatedVerificationCode();
//        user.setVerificationCode(verificationCode);
//        user.setPhoneVerified(false);  // номер не подтвержден
//
//        if (user.getUsername().equals("admin") && user.getPassword().equals("AdminAlliance89107091769")) {
//            user.setRoles(Set.of(Role.ADMIN));
//        } else {
//            user.setRoles(Set.of(Role.USER));
//        }
//        user.setActive(true);
//        myUserDetailsService.register(user);
//
////        smsService.sendSms(user.getPhone(), "Ваш код подтверждения: " + verificationCode);
//
//        return "redirect:/verify-phone";
//    }


//    @GetMapping("/verify-phone")
//    public String verifyPhone() {
//        return "users/verify-phone";
//    }
//
//    // подтверждение
//    @PostMapping("/verify-phone")
//    public String verifyPhone(@RequestParam String code, Principal principal) {
//        Optional<User> user = myUserDetailsService.findByUsername(principal.getName());
//        User newUser = user.get();
//        if (newUser != null && user.get().getVerificationCode().equals(code)) {
//            newUser.setPhoneVerified(true); // подтверждение номера
//            myUserDetailsService.updateUser(newUser);
//            return "redirect:/realty";
//        } else {
//            return "redirect:/verify-phone";
//        }
//    }


    //    @PostMapping("/login")
//    public String login(@RequestParam String name, @RequestParam String password){
//        try{
//            //    создаем объект аутентификации
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(name, password)
//            );
//
//            // устанавливаем аутентификацию в контекст безопасности
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            return "redirect:/realty";
//        }catch (Exception e){
//            return "redirect:/login&error";
//        }
//    }
//    @PostMapping("/register-from-firebase")
//    @ResponseBody
//    public String registerUserFromFirebase(@RequestParam Map<String, String> payload){
//        String idToken = payload.get("idToken");
//        if(idToken == null) {
//            return "error: token is missing";
//        }
//
//        boolean isVerified = firebaseService.verifyIdToken(idToken);
//        if(!isVerified) {
//            return "error: invalid token ";
//        }
//
//        try{
//            FirebaseToken decodedToken = firebaseService.getDecodedToken(idToken);
//            String phone = decodedToken.getClaims().get("phone_number").toString();
//
//            Optional<User> existingUser = myUserDetailsService.findByPhone(phone);
//            if(existingUser.isPresent()) {
//                return "error: phone number already in use";
//            }
//
//            User newUser = new User();
//            newUser.setUsername(phone);
//            newUser.setPhone(phone);
//            newUser.setPassword(passwordEncoder.encode("defaultPassword"));
//
//            newUser.setRoles(Set.of(Role.USER));
//            newUser.setActive(true);
//            newUser.setPhoneVerified(true);
//
//            myUserDetailsService.register(newUser);
//
//            return "success user registered";
//        }catch (Exception e) {
//            return "error: " + e.getMessage();
//        }
//    }


