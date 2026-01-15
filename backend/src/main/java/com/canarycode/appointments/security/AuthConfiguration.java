package com.canarycode.appointments.security;

import com.canarycode.appointments.model.Role;
import com.canarycode.appointments.model.User;
import com.canarycode.appointments.repository.UserRepository;
import com.canarycode.appointments.servicios.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class AuthConfiguration {

    private final UserRepository userRepository;

    public AuthConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean // Se establece cuál será el password encoder por defecto a utilizar por spring
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() throws UsernameNotFoundException {
        return new UserDetailServiceImpl(userRepository);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
