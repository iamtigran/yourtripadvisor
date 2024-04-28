package aua.se.keycloak.helper;

import aua.se.keycloak.dto.AiResponseDto;
import aua.se.keycloak.dto.ConversationDTO;
import aua.se.keycloak.models.Conversation;
import aua.se.keycloak.respository.ConversationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
@Service
@Slf4j
public class DataHelper {


    private final RestTemplate restTemplate = new RestTemplate();

    private final String baseUrl = "https://0b35-37-252-91-114.ngrok-free.app/generate";

    @Autowired
    private ConversationRepository conversationRepository;


    public AiResponseDto callAi(String text) {
        Conversation conversation = new Conversation();
        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the JSON body
        Map<String, Object> body = new HashMap<>();
        body.put("text", text); // Assume "text" is part of the body
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Build the URI with query parameters
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("max_length", 60)
                .queryParam("num_return_sequences", 2)
                .build()
                .toUri();

        // Send the POST request
        AiResponseDto aiResponseDto = restTemplate.postForObject(uri, entity, AiResponseDto.class);

        conversation.setRequest(text);
        assert aiResponseDto != null;
        conversation.setAnswer(aiResponseDto.getOutput().getFirst());
        conversationRepository.save(conversation);

        // Print the response
        System.out.println("Response: " + aiResponseDto);

        return aiResponseDto;
    }
}