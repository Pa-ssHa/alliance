/*
package ru.kozelsk.alliance.services.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.users.CustomOAuth2User;
import ru.kozelsk.alliance.models.users.Role;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.repositories.users.UserRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class GoogleOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    public GoogleOAuth2UserService() {
        log.info("GoogleOAuth2UserService constructor");
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("Starting Google OAuth2 authentication process");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2User attributes: {}", oAuth2User.getAttributes());

        try {
            CustomOAuth2User customUser = new CustomOAuth2User((OidcUser) oAuth2User);
            if (oAuth2User instanceof OidcUser oidcUser) {
                customUser = new CustomOAuth2User(oidcUser);
            } else {
                log.error("Received OAuth2User is not an instance of OidcUser");
                throw new OAuth2AuthenticationException("User is not an OidcUser");
            }

            processOAuth2User(customUser);
            return customUser;
        } catch (ClassCastException e) {
            log.error("Error casting OAuth2User to OidcUser", e);
            throw new OAuth2AuthenticationException("Failed to process OAuth2 user");
        }
    }

    private void processOAuth2User(CustomOAuth2User oAuth2User) {
        String email = oAuth2User.getEmail();
        log.info("Processing OAuth2 user with email: {}", email);

        if (email == null) {
            log.error("Email is null in OAuth2 user attributes");
            return;
        }

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            log.info("User not found, registering new user with email: {}", email);
            registerNewUser(oAuth2User);
        } else {
            log.info("User already exists with email: {}", email);
        }
    }

    private void registerNewUser(CustomOAuth2User oAuth2User) {
        try {
            User user = new User();
            user.setEmail(oAuth2User.getEmail());
            user.setName(oAuth2User.getFullName());
            user.setRoles(Collections.singleton(Role.USER));

            // Для OAuth пользователей можно установить флаг или специальный пароль
            user.setPassword("OAUTH2_USER");
            user.setActive(true);

            User savedUser = userRepository.save(user);
            log.info("Successfully registered new user: {}", savedUser);
        } catch (Exception e) {
            log.error("Error saving new user", e);
        }
    }
}
*/
