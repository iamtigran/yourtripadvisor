package aua.se.keycloak.controller;


import aua.se.keycloak.dto.RegistrationResponse;
import aua.se.keycloak.dto.Role;
import aua.se.keycloak.dto.User;
import aua.se.keycloak.keycloak.KeycloakSecurityUtil;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Tigran Movsesyan
 */
@RestController
@RequestMapping("/api/v1/keycloak")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(ConversationController.class);

    @Autowired
    private KeycloakSecurityUtil keycloakSecurityUtil;

    @Value("${keycloak.realm}")
    private String realm;

    @GetMapping("/users")
    public List<User> getUsers() {
        logger.info("information about all users were requested {} ", "/users");
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
        return mapUsers(userRepresentations);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") String id){
        logger.info("information about a user was requested by id: {} ", id);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        return mapUser(keycloak.realm(realm).users().get(id).toRepresentation());
    }

    @PostMapping("/create/user")
    public ResponseEntity<?> createUser(@RequestBody User user, HttpServletRequest request) {
        System.out.println("Content-Type: " + request.getContentType());
        System.out.println("Request Payload: " + new Gson().toJson(user));

        logger.info("Use was registered by this username: {} ", user.getUserName());
        UserRepresentation userRep = mapUserRep(user);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        Response keycloakResponse = keycloak.realm(realm).users().create(userRep);

        if (keycloakResponse.getStatus() != Response.Status.CREATED.getStatusCode()) {
            return ResponseEntity
                    .status(keycloakResponse.getStatus())
                    .body("Error processing registration: " + keycloakResponse.getStatusInfo().getReasonPhrase());
        }

        RegistrationResponse response = new RegistrationResponse();
        response.setEmail(user.getEmail());
        response.setUserName(user.getUserName());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update/user")
    public Response updateUser(@RequestBody User user) {
        try {
            logger.info("The following user was updated: {}", user.getUserName());
            UserRepresentation userRep = mapUserRep(user);
            Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
            keycloak.realm(realm).users().get(user.getId()).update(userRep);
            return Response.ok(user).build();
        } catch (Exception e) {
            logger.error("The following error {} occurred when updating the following user : {}",e, user.getUserName());
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @PutMapping("/update/password")
    public Response resetPassword(@RequestParam String username, @RequestParam String password) {
        try {
            logger.info("The following user's password was updated: {}", username);
            Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
            UserRepresentation userRep = keycloak.realm(realm).users().search(username).get(0);
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            keycloak.realm(realm).users().get(userRep.getId()).resetPassword(credential);
            return Response.ok("Password reset successfully for user: " + username).build();
        } catch (Exception e) {
            logger.error("The following error {} occurred when updating the user's password : {}",e, username);
            e.printStackTrace();
            return Response.serverError().build();
        }
    }


    @DeleteMapping("/delete/users/{id}")
    public Response deleteUser(@PathVariable("id") String id) {
        logger.info("The following user was deleted: {}", id);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).users().delete(id);
        return Response.ok().build();
    }

    @GetMapping("/user/{id}/roles")
    public List<Role> getRoles(@PathVariable("id") String id){
        logger.info("The following user's roles were requested: {}", id);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        return RoleController.mapRoles(keycloak.realm(realm).users()
                .get(id).roles().realmLevel().listAll());
    }


    private List<User> mapUsers(List<UserRepresentation> userRepresentations) {
        List<User> users = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRepresentations)) {
            userRepresentations.forEach(userRep -> users.add(mapUser(userRep)));
        }
        return users;
    }

    private User mapUser(UserRepresentation userRep) {
        User user = new User();
        user.setId(userRep.getId());
        user.setFirstName(userRep.getFirstName());
        user.setLastName(userRep.getLastName());
        user.setEmail(userRep.getEmail());
        user.setUserName(userRep.getUsername());
        return user;
    }

    private UserRepresentation mapUserRep(User user) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setUsername(user.getUserName());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEmail(user.getEmail());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);

        List<CredentialRepresentation> creds = new ArrayList<>();

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(user.getPassword());
        creds.add(cred);
        userRep.setCredentials(creds);
        return userRep;
    }


}
