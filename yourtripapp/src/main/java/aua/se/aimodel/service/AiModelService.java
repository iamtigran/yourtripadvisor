package aua.se.aimodel.service;
import aua.se.aimodel.dto.AiModelRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

@Service
public class AiModelService {
    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Autowired
    public AiModelService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<?> processRequest(AiModelRequestDTO request) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AiModelRequestDTO> entity = new HttpEntity<>(request, headers);

        String url = "http://localhost:5000/generate";
        ResponseEntity<?> responseEntity = restTemplate.postForEntity(url, entity, String.class);

        return responseEntity;
    }

}
