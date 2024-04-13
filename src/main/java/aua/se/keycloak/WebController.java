package aua.se.keycloak;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController implements IWebController {

    @GetMapping("/chat")
    public String chat() {
        return "redirect:/chat.html";
    }
}
