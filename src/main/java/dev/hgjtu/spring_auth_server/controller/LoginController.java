package dev.hgjtu.spring_auth_server.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Value("${home_server.ip}")
    private String homeServerIp;

    @GetMapping("/login")
    public String login(
            HttpServletRequest request,
            Model model) {

        model.addAttribute("backUrl", "http://" + homeServerIp + ":5050");
        model.addAttribute("registerUrl", "http://" + homeServerIp + ":5050/register");

        return "login";
    }
}
