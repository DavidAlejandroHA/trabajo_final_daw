package com.canarycode.appointments.security;

import com.canarycode.appointments.model.Role;
import com.canarycode.appointments.model.User;
import com.canarycode.appointments.repository.RoleRepository;
import com.canarycode.appointments.repository.UserRepository;
import com.canarycode.appointments.seeders.UsersAndRolesSeeder;
import com.canarycode.appointments.servicios.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

    @Bean // Se establece cuál será el password encoder por defecto a utilizar por spring
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() throws UsernameNotFoundException {
        return new UserDetailServiceImpl(userRepository);
    }

    //https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html
    @Bean
    public AuthenticationProvider authenticationProvider() { // Para procesar autenticaciones de usuarios
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService()); // De que forma puede saber como buscar a un usuario
        authProvider.setPasswordEncoder(passwordEncoder()); // De que forma puede saber como está encriptada la contraseña
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public String seedUsersAndRoles(UserRepository userRepository,
                                    RoleRepository roleRepository) {
        UsersAndRolesSeeder.seedUsersAndRoles(userRepository, roleRepository);
        return "Usuarios y roles creados";
    }
}
