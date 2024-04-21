package aua.se.keycloak.controller;

import aua.se.keycloak.dto.Role;
import aua.se.keycloak.keycloak.KeycloakSecurityUtil;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/keycloak/roles")
public class RoleController {
    @Autowired
    private KeycloakSecurityUtil keycloakSecurityUtil;

    @Value("${realm}")
    private String realm;

    @GetMapping("/")
    public List<Role> getRoles(){
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<RoleRepresentation> roleRepresentations = keycloak.realm(realm).roles().list();
        return mapRoles(roleRepresentations);

    }

    @GetMapping("/{roleName}")
    public Role getRole(@PathVariable("roleName") String roleName){
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        return mapRole(keycloak.realm(roleName).roles().get(roleName).toRepresentation());
    }

    @PostMapping("/create/role")
    public Response createRole(Role role){
        RoleRepresentation roleRep = mapRoleRep(role);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).roles().create(roleRep);
        return Response.ok().build();
    }


    @PutMapping("/update/role")
    public Response updateRole(Role role){
        RoleRepresentation roleRep = mapRoleRep(role);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).roles().get(role.getName()).update(roleRep);
        return Response.ok(role).build();
    }


    @DeleteMapping("/delete/roles/{roleName}")
    public Response deleteRole(@PathVariable("roleName") String roleName){
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).roles().deleteRole(roleName);
        return Response.ok().build();
    }


    public static List<Role> mapRoles(List<RoleRepresentation> roleRepresentations){
        List<Role> roles = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(roleRepresentations)){
            roleRepresentations.forEach(roleRep-> roles.add(mapRole(roleRep)));
        }

        return roles;
    }

    public static Role mapRole(RoleRepresentation roleRep){
        Role role = new Role();
        role.setId(roleRep.getId());
        role.setName(roleRep.getName());
        return role;
    }

    public RoleRepresentation mapRoleRep(Role role){
        RoleRepresentation roleRep = new RoleRepresentation();
        roleRep.setName(role.getName());
        return roleRep;
    }





}
