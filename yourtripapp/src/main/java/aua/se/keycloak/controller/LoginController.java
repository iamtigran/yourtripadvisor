package aua.se.keycloak.controller;

import aua.se.keycloak.dto.LoginCredentials;
import aua.se.keycloak.keycloak.KeycloakSecurityUtil;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class LoginController {



    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;


    private final RestTemplate restTemplate = new RestTemplate();
    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginCredentials credentials) {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(credentials.getUsername())
                    .password(credentials.getPassword())
                    .grantType("password")
                    .build();

            AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to login: " + e.getMessage());
        }
    }


    @PostMapping(path = "/auth/logout")
    public ResponseEntity<?> logoutUser(@RequestParam("refresh_token") String refreshToken) {

        if (refreshToken != null) {
            // Build the URL for Keycloak's logout endpoint
            String logoutEndpoint = serverUrl + "/realms/" + realm + "/protocol/openid-connect/logout";

            // Set headers and content type
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Prepare the body containing the refresh token, client_id, and client_secret
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("refresh_token", refreshToken);

            // Create an HttpEntity with headers and body
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            try {
                // Make the REST call to revoke the refresh token
                ResponseEntity<String> response = restTemplate.postForEntity(logoutEndpoint, request, String.class);

                if (!response.getStatusCode().is2xxSuccessful()) {
                    // Handle unsuccessful revocation here
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("{\"error\": \"Failed to revoke token\"}");
                }

                // Clear the security context
                SecurityContextHolder.clearContext();

                // Return the success response
                return ResponseEntity.ok("{\"message\": \"Logged out successfully\"}");
            } catch (Exception e) {
                // Handle exceptions here
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"Failed to logout: " + e.getMessage() + "\"}");
            }
        } else {
            // Handle missing or invalid refresh token here
            return ResponseEntity.badRequest().body("{\"error\": \"Invalid or missing refresh token.\"}");
        }
    }
}
