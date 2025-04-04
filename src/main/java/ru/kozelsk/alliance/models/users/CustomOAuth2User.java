package ru.kozelsk.alliance.models.users;

import lombok.Getter;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class CustomOAuth2User extends DefaultOidcUser {
    private final String email;

    public CustomOAuth2User(OidcUser oidcUser) {
        super(oidcUser.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo(), "name");
        this.email = oidcUser.getAttribute("email");
    }

    public String getEmail() {
        return email;
    }

    private String extractEmail(OAuth2User oAuth2User, String clientName) {
        if ("google".equalsIgnoreCase(clientName)) {
            return oAuth2User.getAttribute("email");
        }
        return null;
    }

    private String extractName(OAuth2User oAuth2User, String clientName) {
        if ("google".equalsIgnoreCase(clientName)) {
            return oAuth2User.getAttribute("name");
        }
        return null;
    }
}
