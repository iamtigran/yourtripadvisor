package aua.se.keycloak;

import aua.se.aimodel.dto.AiModelRequestDTO;
import aua.se.aimodel.service.AiModelService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.MockitoAnnotations;

public class AiModelServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AiModelService aiModelService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessRequestReturnsValidString() throws Exception {
        // Arrange
        AiModelRequestDTO requestDto = new AiModelRequestDTO("Sample text");
        String jsonResponse = "{\"output\": [\"Expected response text\"]}";
        ResponseEntity<String> mockedResponseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        // Mock RestTemplate behavior
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(mockedResponseEntity);

        // Prepare the mocked JsonNode structure
        JsonNode mockOutputNode = Mockito.mock(JsonNode.class);
        when(mockOutputNode.asText()).thenReturn("Expected response text");

        JsonNode mockRootNode = Mockito.mock(JsonNode.class);
        when(mockRootNode.path("output")).thenReturn(mockOutputNode);
        when(mockRootNode.get(0)).thenReturn(mockOutputNode);

        // Mock ObjectMapper behavior
        when(objectMapper.readTree(any(String.class))).thenReturn(mockRootNode);

        // Act
        ResponseEntity<?> result = aiModelService.processRequest(requestDto);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
