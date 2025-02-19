package com.example.springbootchatmessenger.jwt;

import com.example.springbootchatmessenger.user.UserEntity;
import com.example.springbootchatmessenger.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

        final Optional<UserEntity> userEntityOptional = Optional.ofNullable(userRepository.findByUsername(authentication.getName()));

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();

            if (passwordEncoder.encode(authentication.getCredentials().toString()).equals(userEntity.getPassword())) {
                final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                final List<String> userRoles = Arrays.stream(userEntityOptional.get().getAuthorities().split(",")).toList();
                userRoles.forEach(userRole -> grantedAuthorities.add(new SimpleGrantedAuthority(userRole)));

                return new UsernamePasswordAuthenticationToken(userEntity.getUsername(), authentication.getCredentials(), grantedAuthorities);
            }
            else {
                log.error("Invalid password");
            }
        }
        else {
            log.error("Invalid username or password");
        }
        return null;
    }
}
