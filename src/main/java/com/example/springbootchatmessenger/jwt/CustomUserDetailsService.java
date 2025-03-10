package com.example.springbootchatmessenger.jwt;

import com.example.springbootchatmessenger.exceptions.AuthenticationFailureException;
import com.example.springbootchatmessenger.user.UserEntity;
import com.example.springbootchatmessenger.user.UserRepository;
import com.example.springbootchatmessenger.utility.EncryptionUtil;
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

        final Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);

        User.UserBuilder userBuilder = null;

        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            userBuilder = User
                    .withUsername(userEntity.getUsername())
                    .password(passwordEncoder.encode(userEntity.getPassword()));

            final List<String> authorities = new ArrayList<>();
            Optional.ofNullable(userEntity.getAuthorities()).ifPresent(authority -> {
                authorities.addAll( Arrays.stream(EncryptionUtil.decrypt(authority).split(",")).toList());
            });
            final String[] result = new String[authorities.size()];
            authorities.forEach(authority -> {
                result[authorities.indexOf(authority)] = authority;
            });
            userBuilder.authorities(result);
        }
        else {
            throw new AuthenticationFailureException("User not found");
        }
        return userBuilder.build();
    }
}
