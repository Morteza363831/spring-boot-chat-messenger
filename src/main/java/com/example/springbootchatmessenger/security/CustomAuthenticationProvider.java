package com.example.springbootchatmessenger.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomAuthenticationProvider implements OAuth2UserService<OidcUserRequest, OidcUser> {

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = new OidcUserService().loadUser((OidcUserRequest) userRequest);
        log.info("in custom authentication provider");
        return oidcUser;
    }


}
