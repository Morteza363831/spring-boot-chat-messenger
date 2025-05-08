package com.example.messenger.roles;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.List;

public class ChatAuthorizationService extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;

    public ChatAuthorizationService(Authentication authentication) {
        super(authentication);
    }

    public boolean hasAccess(String authorities) {
        if (getAuthentication() == null || getAuthentication().getAuthorities() == null) {
            return false;
        }
        final List<String> authoritiesList = Arrays.stream(authorities.split(",")).toList();
        return getAuthentication().getAuthorities().stream().anyMatch(grantedAuthority -> authoritiesList.contains(grantedAuthority.getAuthority()));
    }

    public boolean isMatch(String username) {
        if (getAuthentication() == null || getAuthentication().getPrincipal() == null) {
            return false;
        }
        final User user = (User) getAuthentication().getPrincipal();
        return user.getUsername().equals(username);
    }


    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this ;
    }
}
