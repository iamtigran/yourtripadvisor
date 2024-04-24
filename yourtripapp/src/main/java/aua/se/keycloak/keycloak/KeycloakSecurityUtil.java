package aua.se.keycloak.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class KeycloakSecurityUtil {

    private Keycloak keycloak;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.grant-type}")
    private String grantType;

    @Value("${keycloak.name}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    public Keycloak getKeycloakInstance() {
        if (keycloak == null) {  // Corrected this line to check if keycloak is null
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .grantType(grantType)
                    .username(username)
                    .password(password)
                    .build();
        }
        return keycloak;
    }
}
