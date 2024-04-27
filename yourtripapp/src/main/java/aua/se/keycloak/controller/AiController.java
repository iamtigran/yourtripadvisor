package aua.se.keycloak.controller;


import aua.se.keycloak.dto.AiRequestDto;
import aua.se.keycloak.dto.AiResponseDto;
import aua.se.keycloak.helper.DataHelper;
import aua.se.keycloak.respository.ConversationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class AiController {



    private static final Logger logger = LoggerFactory.getLogger(ConversationController.class);



    private final DataHelper dataHelperl;
     @Autowired
    public AiController( DataHelper dataHelperl) {
         this.dataHelperl = dataHelperl;
     }


    @PostMapping("/generate")
    public ResponseEntity<?> aiController(@RequestBody AiRequestDto aiRequestDto){
        AiResponseDto responseDto = dataHelperl.callAi(aiRequestDto.getText());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

}
