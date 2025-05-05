package com.example.springbootchatmessenger.security;

import com.example.springbootchatmessenger.jwt.CustomAuthenticationEntryPoint;
import com.example.springbootchatmessenger.jwt.CustomAuthenticationManager;
import com.example.springbootchatmessenger.jwt.JwtRequestFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

/**
 * SecurityConfig class
 * CSRF disabled
 * CORS enabled
 * some endpoints permitted
 * some must authenticate
 * ExceptionHandler added
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    private final JwtRequestFilter requestFilter;

    public SecurityConfig(final JwtRequestFilter requestFilter) {
        this.requestFilter = requestFilter;
    }

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
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> {
                    httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
                    log.info("Cors applied");
                })
                .exceptionHandling(hehc -> {
                    hehc.accessDeniedHandler(new AccessDeniedHandlerImpl());
                    hehc.authenticationEntryPoint(authenticationEntryPoint());
                    log.info("Access Denied");
                })
                .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    log.info("Session Creation");
                })
                .authenticationManager(authenticationManager())
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(
                                    "/api/v1/users/register", "/api/v1/user", "/login",
                                    "/main/**", "/chat/**", "/ws/**", "/api/v1/auth/token",
                                    "/signup/create", "/css/**", "/js/**", "/messenger-swagger/**",
                                    "/api-docs", "/swagger-ui/**"
                            ).permitAll()
                            .anyRequest()
                            .authenticated();
                    log.info("Authenticated");
                })
                .addFilterAt(requestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new CustomAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**");
    }
}
