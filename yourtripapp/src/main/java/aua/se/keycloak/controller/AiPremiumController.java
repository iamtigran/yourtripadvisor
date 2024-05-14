package aua.se.keycloak.controller;


import aua.se.keycloak.dto.premium.AiRequestPremiumDTO;
import aua.se.keycloak.dto.premium.AiResponsePremiumDTO;
import aua.se.keycloak.helper.DataHelperAdapterPremium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class AiPremiumController {

    private final DataHelperAdapterPremium dataHelperl;
    @Autowired
    public AiPremiumController( DataHelperAdapterPremium dataHelperl) {
        this.dataHelperl = dataHelperl;
    }


    @PostMapping("/generatePremium")
    public ResponseEntity<?> aiController(@RequestBody AiRequestPremiumDTO aiRequestDto){
        AiResponsePremiumDTO responseDto = dataHelperl.callAiPremium(aiRequestDto.getQuestion());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }


}
