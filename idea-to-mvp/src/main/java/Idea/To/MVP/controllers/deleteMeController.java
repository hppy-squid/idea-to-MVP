package Idea.To.MVP.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class deleteMeController {

    @GetMapping("/test")
    public String getDeleteMe() {
        return "redirect:/deleteMe.html";
    }
}
