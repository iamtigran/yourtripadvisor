package aua.se.aimodel.controller;

import aua.se.aimodel.dto.AiModelRequestDTO;
import aua.se.aimodel.service.AiModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generate")
public class AiModelController {

    private final AiModelService aiModelService;

    @Autowired
    public  AiModelController(AiModelService aiModelService){
        this.aiModelService = aiModelService;
    }

    @PostMapping
    public ResponseEntity<?> generateText(@RequestBody AiModelRequestDTO request) {
        try {
            ResponseEntity<?> response = aiModelService.processRequest(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }
}
