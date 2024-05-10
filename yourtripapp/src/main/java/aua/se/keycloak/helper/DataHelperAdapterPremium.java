package aua.se.keycloak.helper;

import aua.se.keycloak.dto.premium.AiResponsePremiumDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
@Service
public class DataHelperAdapterPremium {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String baseUrl = "http://3.72.1.153:8000/query/";


    public AiResponsePremiumDTO callAiPremium(String text) {
        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the JSON body
        Map<String, Object> body = new HashMap<>();
        body.put("question", text); // Assume "text" is part of the body
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Build the URI with query parameters
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .build()
                .toUri();

        // Send the POST request
        AiResponsePremiumDTO aiResponseDto = restTemplate.postForObject(uri, entity, AiResponsePremiumDTO.class);


        //conversationRepository.save(conversation);

        return aiResponseDto;
    }
}
