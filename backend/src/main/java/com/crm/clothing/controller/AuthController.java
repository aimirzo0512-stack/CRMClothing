package com.crm.clothing.controller;

import com.crm.clothing.dto.AuthDtos.*;
import com.crm.clothing.entity.Role;
import com.crm.clothing.entity.User;
import com.crm.clothing.repository.RoleRepository;
import com.crm.clothing.repository.UserRepository;
import com.crm.clothing.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthController(AuthenticationManager a, UserRepository u, RoleRepository r,
                          PasswordEncoder e, JwtUtil j) {
        this.authManager = a; this.users = u; this.roles = r; this.encoder = e; this.jwt = j;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username, req.password));
        User u = users.findByUsername(req.username).orElseThrow();
        Set<String> roleNames = u.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        String token = jwt.generate(u.getUsername(), roleNames);
        return ResponseEntity.ok(new AuthResponse(token, u.getUsername(), u.getFullName(), roleNames));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (users.existsByUsername(req.username))
            return ResponseEntity.badRequest().body(java.util.Map.of("error","Username already exists"));
        String roleName = (req.role == null || req.role.isBlank()) ? "EMPLOYEE" : req.role.toUpperCase();
        Role role = roles.findByName(roleName).orElseGet(() -> roles.save(new Role(roleName)));
        User u = new User();
        u.setUsername(req.username);
        u.setFullName(req.fullName);
        u.setPassword(encoder.encode(req.password));
        u.setRoles(Set.of(role));
        users.save(u);
        Set<String> roleNames = Set.of(roleName);
        String token = jwt.generate(u.getUsername(), roleNames);
        return ResponseEntity.ok(new AuthResponse(token, u.getUsername(), u.getFullName(), roleNames));
    }
}
