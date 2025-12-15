package com.speakerhive.project2.controller;

import com.speakerhive.project2.model.Session;
import com.speakerhive.project2.service\SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final SessionService service;

    public HomeController(SessionService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(HttpSession httpSession, Model model) {
        String userName = (String) httpSession.getAttribute("userName");
        if (userName == null) {
            return "redirect:/login";
        }
        List<Session> sessions = service.getSessions();
        model.addAttribute("sessions", sessions);
        model.addAttribute("userName", userName);
        return "index";
    }

    @PostMapping("/assign")
    @ResponseBody
    public ResponseEntity<?> assignRole(@RequestParam("sessionIndex") int sessionIndex,
                                        @RequestParam("roleIndex") int roleIndex,
                                        HttpSession httpSession) {
        String userName = (String) httpSession.getAttribute("userName");
        if (userName == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Not logged in"));
        }
        String assigned = service.assignRole(sessionIndex, roleIndex, userName);
        if (assigned == null) {
            return ResponseEntity.ok(Map.of("success", false, "message", "Role already assigned"));
        }
        Map<String,Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("assignedTo", assigned);
        resp.put("currentUser", userName);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/unassign")
    @ResponseBody
    public ResponseEntity<?> unassignRole(@RequestParam("sessionIndex") int sessionIndex,
                                          @RequestParam("roleIndex") int roleIndex,
                                          HttpSession httpSession) {
        String userName = (String) httpSession.getAttribute("userName");
        if (userName == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Not logged in"));
        }
        boolean ok = service.unassignRole(sessionIndex, roleIndex, userName);
        if (!ok) return ResponseEntity.ok(Map.of("success", false, "message", "Unable to unassign"));
        return ResponseEntity.ok(Map.of("success", true));
    }
}
