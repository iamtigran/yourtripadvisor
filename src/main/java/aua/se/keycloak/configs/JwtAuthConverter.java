package aua.se.keycloak.configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;

/**
     @Author: Tigran Movsesyan

 */

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Autowired
    private ObjectMapper mapper;
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt){
        Collection<GrantedAuthority> roles = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt,roles);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        if (Objects.nonNull(jwt.getClaim("realm_access"))) {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            List<String> keycloakRoles = mapper.convertValue(realmAccess.get("roles"), new TypeReference<List<String>>() {});
            Set<GrantedAuthority> roles = new HashSet<>();

            for (String keycloakRole : keycloakRoles) {
                roles.add(new SimpleGrantedAuthority(keycloakRole));
                System.out.println("Adding role: " + keycloakRole);
            }
            return roles;
        }
        return new ArrayList<>();
    }



}
