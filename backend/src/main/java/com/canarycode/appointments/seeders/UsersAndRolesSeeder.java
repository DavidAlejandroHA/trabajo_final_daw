package com.canarycode.appointments.seeders;

import com.canarycode.appointments.model.Role;
import com.canarycode.appointments.model.User;
import com.canarycode.appointments.repository.RoleRepository;
import com.canarycode.appointments.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class UsersAndRolesSeeder {
    public static void seedUsersAndRoles(UserRepository userRepository, RoleRepository roleRepository) {
        // Obtener roles del repositorio de roles
        Role roleClient = roleRepository.findByName("ROLE_CLIENT");
        Role roleProfessional = roleRepository.findByName("ROLE_PROFESSIONAL");
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");

        // Si no existen los roles, se crean
        if (roleClient == null) { roleClient = roleRepository.save(new Role("ROLE_CLIENT")); }
        if (roleProfessional == null) { roleProfessional = roleRepository.save(new Role("ROLE_PROFESSIONAL")); }
        if (roleAdmin == null) { roleAdmin =  roleRepository.save(new Role("ROLE_ADMIN")); }

        // Crear usuarios basic, premium y admin
        User userCliente = new User("Cliente",
                "cliente@gmail.com",new BCryptPasswordEncoder().encode("1234"), List.of(roleClient));
        User userProfessional = new User("Professional",
                "professional@gmail.com",new BCryptPasswordEncoder().encode("12345"), List.of(roleClient, roleProfessional));
        User userAdmin = new User("Admin",
                "admin@gmail.com",new BCryptPasswordEncoder().encode("123"), List.of(roleClient, roleAdmin));

        createUser(userCliente, userRepository);
        createUser(userProfessional, userRepository);
        createUser(userAdmin, userRepository);
    }

    /**
     * Creación de usuarios
     * @param user
     * @param userRepository
     */
    private static void createUser(User user, UserRepository userRepository) {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            userRepository.save(user);
            System.out.println("El usuario " + user.getUsername() + " ha sido creado");
        } else {
            System.out.println("El usuario " + user.getUsername() + " ya ha sido creado previamente");
        }
    }
}