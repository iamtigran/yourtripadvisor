package aua.se.keycloak.controller;

import aua.se.keycloak.dto.ConversationDTO;
import aua.se.keycloak.models.Conversation;
import aua.se.keycloak.respository.ConversationRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController {

    private static final Logger logger = LoggerFactory.getLogger(ConversationController.class);
    private final ConversationRepository conversationRepository;

    @Autowired
    public ConversationController(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('administrator')")
    public List<Conversation> getConversations() {
        return conversationRepository.findAll();
    }


    @PostMapping("/post")
    public ResponseEntity<Conversation> postConversation(@RequestBody ConversationDTO conversationDTO) {
        logger.info("Received conversation with question: {}", conversationDTO.getRequest());

        Conversation conversationToBeSaved = new Conversation();
        conversationToBeSaved.setRequest(conversationDTO.getRequest());
        conversationToBeSaved.setAnswer(conversationDTO.getAnswer());

        logger.info("Saving conversation: {}", conversationToBeSaved);
        Conversation savedConversation = conversationRepository.save(conversationToBeSaved);
        logger.info("Saved conversation: {}", savedConversation);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedConversation);
    }

    @Transactional
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('administrator')")
    public ResponseEntity<?> deleteConversation(@PathVariable Long id) {
        Optional<Conversation> conversation = conversationRepository.findById(id);
        ConversationDTO conversationDTO = new ConversationDTO();
        if(conversation.isPresent()) {
            conversationDTO.setRequest(conversation.get().getRequest());
            conversationDTO.setAnswer(conversation.get().getAnswer());
        }
        conversation.ifPresent(conversationRepository::delete);
        return ResponseEntity.ok(conversationDTO);
    }


}
