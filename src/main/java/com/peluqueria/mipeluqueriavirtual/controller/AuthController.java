package com.peluqueria.mipeluqueriavirtual.controller;

import com.peluqueria.mipeluqueriavirtual.dto.AuthResponse;
import com.peluqueria.mipeluqueriavirtual.dto.GoogleAuthRequest;
import com.peluqueria.mipeluqueriavirtual.dto.LoginRequest;
import com.peluqueria.mipeluqueriavirtual.dto.RegisterRequest;
import com.peluqueria.mipeluqueriavirtual.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST /api/auth/registro
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegisterRequest req) {
        try {
            AuthResponse res = authService.registrar(req);
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            AuthResponse res = authService.login(req);
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // POST /api/auth/google — login o registro automatico con Google
    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestBody GoogleAuthRequest req) {
        try {
            AuthResponse res = authService.loginConGoogle(req.getToken());
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
