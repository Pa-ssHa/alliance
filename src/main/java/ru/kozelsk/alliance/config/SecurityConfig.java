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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;


@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

//    @Autowired
//    private MyUserDetailsService myUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


       http
               .csrf(csrf -> csrf
                       .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // CSRF токен в куки
               )

                .authorizeRequests(auth -> auth
//                        .requestMatchers("/excursion/**").hasRole("ADMIN")
//                        .requestMatchers("/realty/new", "/realty/edit/**", "/realty/delete/**").hasRole("ADMIN")
                        .requestMatchers("/realty/new", "/realty/edit/**", "/realty/delete/**").authenticated()
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


    @Bean
    public AuthenticationSuccessHandler successHandler(){
       return new SimpleUrlAuthenticationSuccessHandler("/realty");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
       return authenticationConfiguration.getAuthenticationManager();
    }

}
