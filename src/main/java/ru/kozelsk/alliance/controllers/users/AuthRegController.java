package ru.kozelsk.alliance.controllers.users;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.users.FirebaseService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;
import ru.kozelsk.alliance.utils.dto.RegistrationResponse;

import java.util.Map;
import java.util.Optional;

@RestController
public class AuthRegController {

    private final MyUserDetailsService myUserDetailsService;
    private final FirebaseService firebaseService;

    @Autowired
    public AuthRegController(MyUserDetailsService myUserDetailsService, FirebaseService firebaseService) {
        this.myUserDetailsService = myUserDetailsService;
        this.firebaseService = firebaseService;
    }


/*
    @PostMapping("/register-from-firebase")
    public ResponseEntity<RegistrationResponse> registerFromFirebase(
            @RequestBody Map<String, String> payload) {

        System.out.println("Запрос получен в контроллере");

        String idToken = payload.get("idToken");
        if (idToken == null) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(false, "ID token is missing"));
        }

        // Проверяем ID токен
        boolean isVerified = firebaseService.verifyIdToken(idToken);
        if (!isVerified) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(false, "Invalid ID token"));
        }

        // Получаем данные пользователя из токена
        try {
            FirebaseToken decodedToken = firebaseService.getDecodedToken(idToken);
            String phoneNumber = decodedToken.getClaims().get("phone_number").toString();

            System.out.println("Полученный номер телефона: " + phoneNumber);

            // Проверяем, существует ли пользователь с таким номером телефона
            Optional<User> existingUser = myUserDetailsService.findByPhone(phoneNumber);
            if (existingUser.isPresent()) {

                System.out.println("Пользователь с таким номером уже существует: " + existingUser.get().getPhone());

                return ResponseEntity.ok().body(new RegistrationResponse(false, "Phone number already in use"));
            }

            System.out.println("Номер уникальный, можно регистрировать");
            return ResponseEntity.ok().body(new RegistrationResponse(true, "User registered successfully"));
        } catch (Exception e) {
            System.out.println("Ошибка при обработке токена: " + e.getMessage());
            return ResponseEntity.badRequest().body(new RegistrationResponse(false, "Error: " + e.getMessage()));
        }
    }
*/


/*
    @PostMapping("/registration/checkPhone")
    public ResponseEntity<RegistrationResponse> checkPhone(@RequestParam String phone){

        if (myUserDetailsService.findByPhone(phone).isPresent()){
            return ResponseEntity.ok().body(new RegistrationResponse(false, "Phone number already in use"));
        }
        return ResponseEntity.ok().body(new RegistrationResponse(true, "User registered successfully"));
    }
*/


    @PostMapping("/registration/checkPhone")
    public ResponseEntity<RegistrationResponse> checkPhone(@RequestBody Map<String, String> payload){

        String phone = payload.get("phone");

        if (phone == null) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(false, "Phone number is missing"));
        }


        if (myUserDetailsService.findByPhone(phone).isPresent()) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(false, "Phone number already in use"));
        }
        return ResponseEntity.ok().body(new RegistrationResponse(true, "User registered successfully"));
    }





    /*@PostMapping("/register-from-firebase")
    public ResponseEntity<RegistrationResponse> registerFromFirebase(
            @RequestBody Map<String, String> payload) {

        System.out.println("Запрос получен в контроллере");

        String idToken = payload.get("idToken");
        if (idToken == null) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(false, "ID token is missing"));
        }

        // Проверяем ID токен
        FirebaseToken decodedToken;
        try {
            decodedToken = firebaseService.getDecodedToken(idToken);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(false, "Invalid ID token"));
        }

        // Получаем номер телефона из токена
        String phoneNumber = (String) decodedToken.getClaims().get("phone_number");
        if (phoneNumber == null) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(false, "Phone number not found in token"));
        }

        System.out.println("Полученный номер телефона: " + phoneNumber);

        // Проверяем, существует ли пользователь с таким номером телефона
        Optional<User> existingUser = myUserDetailsService.findByPhone(phoneNumber);
        if (existingUser.isPresent()) {

            System.out.println("Пользователь с таким номером уже существует: " + existingUser.get().getPhone());
            return ResponseEntity.ok().body(new RegistrationResponse(false, "Phone number already in use"));
        }

        System.out.println("Номер уникальный, можно регистрировать");

        return ResponseEntity.ok().body(new RegistrationResponse(true, "User registered successfully"));
    }*/
}
