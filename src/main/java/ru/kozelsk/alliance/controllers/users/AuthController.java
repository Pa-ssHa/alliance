package ru.kozelsk.alliance.controllers.users;

import com.google.firebase.auth.FirebaseToken;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kozelsk.alliance.models.users.Role;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.users.FirebaseService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
public class AuthController {

    private final MyUserDetailsService myUserDetailsService;
    private final FirebaseService firebaseService;

    @Autowired
    public AuthController(MyUserDetailsService myUserDetailsService, FirebaseService firebaseService) {
        this.myUserDetailsService = myUserDetailsService;
        this.firebaseService = firebaseService;
    }

    @PostMapping("/register-from-firebase")
    public ResponseEntity<Map<String, Boolean>> registerFromFirebase(
            @RequestBody Map<String, String> payload) {

        String idToken = payload.get("idToken");
        if (idToken == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false));
        }

        // Проверяем ID токен
        boolean isVerified = firebaseService.verifyIdToken(idToken);
        if (!isVerified) {
            return ResponseEntity.badRequest().body(Map.of("success", false));
        }

        // Получаем данные пользователя из токена
        try {
            FirebaseToken decodedToken = firebaseService.getDecodedToken(idToken);
            String phoneNumber = decodedToken.getClaims().get("phone_number").toString();

            // Проверяем, существует ли пользователь с таким номером телефона
            Optional<User> existingUser = myUserDetailsService.findByPhone(phoneNumber);
            if (existingUser.isPresent()) {
                return ResponseEntity.ok(Map.of("success", false));
            }

            // Создаем нового пользователя
            User newUser = new User();
            newUser.setPhone(phoneNumber);
            newUser.setUsername(phoneNumber); // Используем номер телефона как имя пользователя
            newUser.setPassword("defaultPassword"); // Установите безопасный пароль
            newUser.setRoles(Set.of(Role.ROLE_USER));
            newUser.setActive(true);
            newUser.setPhoneVerified(true);

            myUserDetailsService.register(newUser);

            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false));
        }
    }
}
