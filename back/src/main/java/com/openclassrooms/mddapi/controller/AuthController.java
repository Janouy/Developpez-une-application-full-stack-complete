package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.response.ApiResponse;
import com.openclassrooms.mddapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoints d'authentification (inscription, connexion). */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;

    /** @param service service d'authentification */
    public AuthController(AuthService service) {
        this.service = service;
    }

    /**
     * Inscription ; renvoie un JWT.
     * @param req payload d'inscription validé
     * @return 200 OK avec un message de succés
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid RegisterRequest req) {
        ApiResponse response = service.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Connexion ; renvoie un JWT.
     * @param req identifiants de connexion validés
     * @return 200 OK avec le token et l'utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
        return ResponseEntity.ok(service.login(req));
    }


}

