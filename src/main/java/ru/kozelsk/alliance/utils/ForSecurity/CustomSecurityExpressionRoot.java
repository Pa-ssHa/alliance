/*
package ru.kozelsk.alliance.utils.ForSecurity;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import ru.kozelsk.alliance.utils.services.CheckAuthorizeService;

public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private final CheckAuthorizeService checkAuthorizeService;
    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomSecurityExpressionRoot(Authentication authentication, CheckAuthorizeService checkAuthorizeService) {
        super(authentication);
        this.checkAuthorizeService = checkAuthorizeService;
    }

    public boolean isAdmin() {
        return checkAuthorizeService.checkAdmin(this.getAuthentication());
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getThis() {
        return target;
    }

    public void setThis(Object target) {
        this.target = target;
    }
}
*/
