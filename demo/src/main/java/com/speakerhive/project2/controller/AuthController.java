package com.speakerhive.project2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "email", required = false) String email,
                          HttpSession session) {

        // prefer 'name' param; fall back to email (simple demo)
        String user = (name != null && !name.isBlank()) ? name.trim() :
                      (email != null && !email.isBlank()) ? email.trim() : "Guest";
        session.setAttribute("userName", user);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
