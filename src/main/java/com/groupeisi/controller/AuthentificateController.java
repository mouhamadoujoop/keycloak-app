package com.groupeisi.controller;

import com.groupeisi.request.AuthRequest;
import com.groupeisi.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("keycloackapp/api")
public class AuthentificateController {
    @Autowired
    private AuthService authService;
    private static final String MESSAGE = "message";
    private static final String STATUT = "responseCode";
    @PostMapping("/auth")
    public ResponseEntity<Object> login(@RequestBody AuthRequest authRequest) {
        Map<String, Object> output = new HashMap<>();
        try {
            output.put("access_token", authService.login(authRequest));
            output.put(STATUT, 200);
            output.put(MESSAGE, "Authentification réussie avec succes ...");
            return ResponseEntity.ok(output);
        } catch (Exception e) {
            log.info(e.toString());
            output.put(MESSAGE, e.getMessage());
            output.put(STATUT, 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(output);
        }
    }
    @GetMapping("/auth")
    public ResponseEntity<Object> logins() {
        Map<String, Object> output = new HashMap<>();
        try {
//            output.put("access_token", authService.login(authRequest));
//            output.put(STATUT, 200);
//            output.put(MESSAGE, "Authentification réussie avec succes ...");
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            log.info(e.toString());
            output.put(MESSAGE, e.getMessage());
            output.put(STATUT, 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(output);
        }
    }

}
