package aua.se.keycloak.dto;

import lombok.Data;

@Data
public class RegistrationResponse {
        private String firstName;
        private String lastName;
        private String email;
        private String userName;


}
