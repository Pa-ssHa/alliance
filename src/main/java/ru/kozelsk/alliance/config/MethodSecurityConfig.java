/*
package ru.kozelsk.alliance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import ru.kozelsk.alliance.components.IsAdminAnnotationHandler;
import ru.kozelsk.alliance.utils.services.CheckAuthorizeService;

@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {

     @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(CheckAuthorizeService checkAuthorizeService) {
        return new IsAdminAnnotationHandler(checkAuthorizeService);
    }
}
*/
