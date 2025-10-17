package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.error.EmailAlreadyInUseException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Service d'authentification (register, login, génération JWT). */
@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final AuthenticationManager authManager;

    /** @param repo repository user ; @param encoder hash des mots de passe ; @param jwt service JWT ; @param am auth manager */
    public AuthService(UserRepository repo, PasswordEncoder encoder, JwtService jwt, AuthenticationManager am) {
        this.repo = repo; this.encoder = encoder; this.jwt = jwt; this.authManager = am;
    }

    /**
     * Inscrire un nouvel utilisateur et renvoyer un JWT.
     * @param req données d'inscription (name, email, password)
     * @return réponse contenant la réponse de l'api
     * @throws EmailAlreadyInUseException si l'email est déjà utilisé
     */
    public ApiResponse register(RegisterRequest req) {
        if (repo.existsByEmail(req.email()) || repo.existsByName(req.name())) {
            throw new EmailAlreadyInUseException();
        }

        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email());
        user.setPassword(encoder.encode(req.password()));

        repo.save(user);

        return new ApiResponse(HttpStatus.CREATED.value(), "User registered successfully");
    }

    /**
     * Authentifier un utilisateur et renvoyer un JWT.
     * @param req identifiants (email ou nom, password)
     * @return réponse contenant le token
     * @throws AuthenticationException si les identifiants sont invalides
     */
    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.login(), req.password()));

        var user = repo.findByEmail(req.login()).or(() -> repo.findByName(req.login())).orElseThrow();

        String token = jwt.generate(user.getName() != null ? user.getName() : user.getEmail());
        return new AuthResponse(token);
    }
}
