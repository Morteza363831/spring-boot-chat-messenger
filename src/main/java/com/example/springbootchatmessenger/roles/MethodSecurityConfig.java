package com.example.springbootchatmessenger.roles;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final CustomMethodSecurityExpressionHandler customMethodSecurityExpressionHandler;

    public MethodSecurityConfig(CustomMethodSecurityExpressionHandler customMethodSecurityExpressionHandler) {
        this.customMethodSecurityExpressionHandler = customMethodSecurityExpressionHandler;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return customMethodSecurityExpressionHandler;
    }
}