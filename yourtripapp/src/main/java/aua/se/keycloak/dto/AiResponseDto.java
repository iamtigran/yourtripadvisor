package aua.se.keycloak.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiResponseDto {
    private List<String> output;
}
