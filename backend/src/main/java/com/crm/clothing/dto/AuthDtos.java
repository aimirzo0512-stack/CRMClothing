package com.crm.clothing.dto;

public class AuthDtos {
    public static class LoginRequest {
        public String username;
        public String password;
    }
    public static class RegisterRequest {
        public String username;
        public String password;
        public String fullName;
        public String role; // ADMIN or EMPLOYEE
    }
    public static class AuthResponse {
        public String token;
        public String username;
        public String fullName;
        public java.util.Set<String> roles;
        public AuthResponse(String t, String u, String f, java.util.Set<String> r) {
            this.token = t; this.username = u; this.fullName = f; this.roles = r;
        }
    }
}
