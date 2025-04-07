/*
package ru.kozelsk.alliance.components;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.kozelsk.alliance.utils.ForSecurity.CustomSecurityExpressionRoot;
import ru.kozelsk.alliance.utils.services.CheckAuthorizeService;

public class IsAdminAnnotationHandler extends DefaultMethodSecurityExpressionHandler {

    private final CheckAuthorizeService checkAuthorizeService;

    public IsAdminAnnotationHandler(CheckAuthorizeService checkAuthorizeService) {
        this.checkAuthorizeService = checkAuthorizeService;
    }

//    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication) {
//        CustomSecurityExpressionRoot root = new CustomSecurityExpressionRoot(authentication, checkAuthorizeService);
//        root.setPermissionEvaluator(getPermissionEvaluator());
//        return root;
//    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        CustomSecurityExpressionRoot root = new CustomSecurityExpressionRoot(authentication, checkAuthorizeService);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setThis(invocation.getThis());
        return root;
    }

}*/
