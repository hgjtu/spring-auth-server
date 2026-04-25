package dev.hgjtu.spring_auth_server.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(
            HttpServletRequest request,
            Model model) {

        model.addAttribute("backUrl", "http://localhost:5050");
        model.addAttribute("registerUrl", "http://localhost:5050/register");

        return "login";
    }
}
