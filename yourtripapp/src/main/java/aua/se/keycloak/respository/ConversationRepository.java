package aua.se.keycloak.respository;

import aua.se.keycloak.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation,Long> {
    List<Conversation> findAllById(Long id);
}
