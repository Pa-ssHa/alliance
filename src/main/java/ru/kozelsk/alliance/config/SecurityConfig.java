package ru.kozelsk.alliance.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import ru.kozelsk.alliance.models.users.Role;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.repositories.users.UserRepository;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService myUserDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


       http
               .csrf(csrf -> csrf
                       .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // CSRF токен в куки
               )

                .authorizeRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/realty/new", "/realty/edit/**", "/realty/delete/**").hasRole("ADMIN")
                        .requestMatchers("/temp").authenticated()
//                        .requestMatchers("/realty/new", "/realty/edit/**", "/realty/delete/**").authenticated()
                        .anyRequest().permitAll()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/realty", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
               .oauth2Login(oauth -> oauth
                       .loginPage("/login")
                       .defaultSuccessUrl("/registration/api/oauth2", true)
               )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/realty")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
               .sessionManagement(session -> session
                        .sessionFixation().migrateSession() // Защита от фиксации сессии
                        .maximumSessions(1) // Не более одной сессии на пользователя
                        .expiredUrl("/login?expired=true")
                );

        return http.build();
    }


//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(MyUserDetailsService myUserDetailsService) {
//        return userRequest -> {
//            OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
//
//            String email = oAuth2User.getAttribute("email");
//            Optional<User> userOptional = myUserDetailsService.findByEmail(email);
//
//            if (userOptional.isEmpty()) {
//                throw new UsernameNotFoundException("Пользователь не найден: " + email);
//            }
//
//            User user = userOptional.get();
//
//            List<GrantedAuthority> authorities = user.getRoles().stream()
//                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
//                    .collect(Collectors.toList());
//
//            // Добавляем OIDC_USER, чтобы не терять стандартные атрибуты
//            authorities.add(new SimpleGrantedAuthority("OIDC_USER"));
//
//            return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "email");
//        };
//    }

//    @Bean
//    public UserDetailsService userDetailsService(MyUserDetailsService userDetailsService) {
//        return username -> {
//            // Ищем пользователя по email (или name, в зависимости от логики)
//            Optional<User> user = userDetailsService.findByName(username); // или findByEmail(username)
//            if (user.isEmpty()) throw new UsernameNotFoundException(username);
//
//            return org.springframework.security.core.userdetails.User
//                    .withUsername(user.get().getEmail()) // или getName(), если вход по имени
//                    .password(user.get().getPassword()) // важно вернуть реальный пароль!
//                    .roles(user.get().getRoles().stream().map(Role::name).toArray(String[]::new))
//                    .build();
//        };
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    }

    @Bean
    public AuthenticationSuccessHandler successHandler(){
       return new SimpleUrlAuthenticationSuccessHandler("/realty");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
       return authenticationConfiguration.getAuthenticationManager();
    }

    /*@Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtAuthenticationConverter.setPrincipalClaimName("preferred_username");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            var roles =jwt.getClaimAsStringList("spring_sec_roles");

            return Stream.concat(authorities.stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();
        });
        return jwtAuthenticationConverter;
    }*/

    /*@Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        var oidcUserService = new OidcUserService();
        return userRequest -> {
            var oidcUser = oidcUserService.loadUser(userRequest);
            var roles = oidcUser.getClaimAsStringList("spring_sec_roles");
            var authorities = Stream.concat(oidcUser.getAuthorities().stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();

            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }*/

}
