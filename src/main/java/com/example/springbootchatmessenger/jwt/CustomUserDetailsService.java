package com.example.springbootchatmessenger.jwt;

import com.example.springbootchatmessenger.user.UserEntity;
import com.example.springbootchatmessenger.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        final Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.findByUsername(username));

        User.UserBuilder userBuilder = null;

        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            userBuilder = User
                    .withUsername(userEntity.getUsername())
                    .password(passwordEncoder.encode(userEntity.getPassword()));

            final List<String> authorities = new ArrayList<>();
            Optional.ofNullable(userEntity.getAuthorities()).ifPresent(authority -> {
                authorities.addAll( Arrays.stream(authority.split(",")).toList());
            });
            final String[] result = new String[authorities.size()];
            authorities.forEach(authority -> {
                result[authorities.indexOf(authority)] = authority;
            });
            userBuilder.authorities(result);
        }
        else {
            log.error("User not found with username {}", username);
        }

        return userBuilder.build();
    }
}
