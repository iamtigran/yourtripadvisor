package aua.se.keycloak.controller;



import aua.se.keycloak.keycloak.KeycloakSecurityUtil;
import jakarta.ws.rs.NotFoundException;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/premium")
@CrossOrigin
        (origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class PremiumController {

    @Autowired
    private KeycloakSecurityUtil keycloakSecurityUtil;


    @Value("${keycloak.realm}")
    private String realm;


    private ArrayList<String> codes = new ArrayList();
    @PostMapping("/upgrade")


    public ResponseEntity<?> upgradeUser(String username, String coupon){
        codes.add("kasjkajssaas");
        codes.add("dlskslddsssd");
        codes.add("sasssasa");

        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        UsersResource usersResource = keycloak.realm(realm).users();
        UserRepresentation userRepresentation = usersResource.search(username).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));

         if(codes.contains(coupon)){

            // Retrieve the UserResource to modify user roles
            UserResource userResource = usersResource.get(userRepresentation.getId());

            // Fetch the role from the realm roles, assuming it already exists
            RoleRepresentation roleToAdd = keycloak.realm(realm).roles().get("userPremium").toRepresentation();
            if (roleToAdd == null) {
                throw new NotFoundException("Role not found");
            }

            // Assign the role to the user
            userResource.roles().realmLevel().add(Collections.singletonList(roleToAdd));

            // Fetch new role list to return in the response
            List<RoleRepresentation> updatedRoles = userResource.roles().realmLevel().listAll();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(updatedRoles);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Code is invalid");
    }


}
