package com.crm.clothing.config;

import com.crm.clothing.entity.Role;
import com.crm.clothing.entity.User;
import com.crm.clothing.repository.RoleRepository;
import com.crm.clothing.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(RoleRepository roles, UserRepository users, PasswordEncoder enc) {
        return args -> {
            Role admin = roles.findByName("ADMIN").orElseGet(() -> roles.save(new Role("ADMIN")));
            Role emp   = roles.findByName("EMPLOYEE").orElseGet(() -> roles.save(new Role("EMPLOYEE")));

            if (!users.existsByUsername("admin")) {
                User u = new User();
                u.setUsername("admin");
                u.setFullName("Administrator");
                u.setPassword(enc.encode("admin123"));
                u.setRoles(Set.of(admin));
                users.save(u);
            }
            if (!users.existsByUsername("employee")) {
                User u = new User();
                u.setUsername("employee");
                u.setFullName("Store Employee");
                u.setPassword(enc.encode("employee123"));
                u.setRoles(Set.of(emp));
                users.save(u);
            }
        };
    }
}
