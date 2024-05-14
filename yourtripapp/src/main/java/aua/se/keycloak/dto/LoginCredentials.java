package aua.se.keycloak.dto;

import lombok.Data;

@Data
public class LoginCredentials {
    private String username;
    private String password;
}
