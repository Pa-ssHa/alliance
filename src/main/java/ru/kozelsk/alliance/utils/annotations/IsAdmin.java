package ru.kozelsk.alliance.utils.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})     // I can use for methods and classes
@Retention(RetentionPolicy.RUNTIME)     //  It will be available in runtime throw reflection
@Documented
@PreAuthorize("@customSecurity.checkAdmin(authentication)")
public @interface IsAdmin {
}
