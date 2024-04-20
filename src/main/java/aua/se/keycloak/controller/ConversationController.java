package aua.se.keycloak.controller;

import aua.se.keycloak.models.Conversation;
import aua.se.keycloak.respository.ConversationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController {

    @Autowired
    private ConversationRepository conversationRepository;

    @GetMapping("/get")
    public List<Conversation> getConversations(){
        return conversationRepository.findAll();
    }


    @Transactional
    @PostMapping("/post")
    public ResponseEntity<?>  postConversation(@RequestBody Conversation conversation){
        conversationRepository.save(conversation);
        return ResponseEntity.ok(conversation) ;
    }


    @Transactional
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('administrator')")
    public ResponseEntity<?>  deleteConversation(@PathVariable Long id){
        Optional<Conversation> conversation = conversationRepository.findById(id);

        conversation.ifPresent(value -> conversationRepository.delete(value));

        return ResponseEntity.ok(conversation) ;
    }


}
