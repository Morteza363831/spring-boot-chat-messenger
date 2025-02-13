package com.example.springbootchatmessenger.security;

//import com.example.springbootchatmessenger.keycloak.GrantedAuthoritiesMapperImpl; <-- to handle roles
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/*
 * this class will handle security of the project
 * --> authenticationSuccessHandler will handle redirection after successful login
 * --> securityFilterChain will handle requests for endpoints (give access to them or authenticate them before giving access)
 * --> webSecurityCustomizer will ignore h2-console (so we can connect to h2 database and check tables)
 */

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.example")
public class SecurityConfig {


    private final CustomAuthenticationProvider customAuthenticationProvider;

    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    /*@Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

        System.out.println(customAuthenticationProvider.toString());
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }*/


    // below method wil handle authorities for each user !
    /*@Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return new GrantedAuthoritiesMapperImpl();
    }*/

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                OidcUser user = (OidcUser) authentication.getPrincipal();
                
                // Construct the redirect URL
                String redirectUrl = "/main/" + user.getPreferredUsername(); // Construct the redirect URL

                // Perform the redirect
                response.sendRedirect(redirectUrl); // Redirect to the main page
            }
        };
    }


    /*
     * authentication method for endpoints will config here !
     * --> authorizerHttpRequests will give access to some endpoints like (/signup and its front files) and authenticate some other endpoints
     * --> oauth2Login  will authenticate users before anything and then if user has a successful login will redirect it to main page)
     * --> csrf must be disabled here for h2-console
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/signup","/css/signUp.css","/js/signUp.js","/signup/create").permitAll()
                                .anyRequest().authenticated())
                .oauth2Login(oauth2 -> {
                    oauth2
                            .userInfoEndpoint(userInfoEndpointConfig -> {

                                userInfoEndpointConfig
                                        .oidcUserService(customAuthenticationProvider);
                                        //.userAuthoritiesMapper(this.userAuthoritiesMapper()); <-- call custom authority checker
                            })
                            .successHandler(authenticationSuccessHandler());
                })
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**");
    }
}
