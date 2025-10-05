package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ProfileResponse;
import com.openclassrooms.mddapi.dto.UpdateProfileRequest;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.error.EmailAlreadyInUseException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.error.UserNotFoundException;

/** Service utilisateur (lecture profil, mise à jour profil). */
@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /** @param repo repo utilisateurs ; @param passwordEncoder encodeur de mots de passe ; @param jwtService service JWT */
    public UserService(UserRepository repo, PasswordEncoder passwordEncoder,JwtService jwtService) {
        this.repo = repo; this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Récupérer un utilisateur par email.
     * @param email email de l'utilisateur
     * @return profil utilisateur minimal (nom, email)
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     */
    public UserResponse getByEmail(String email) {
        User u = repo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + email + "' not found"));

        return new UserResponse(u.getName(), u.getEmail());
    }

    /**
     * Mettre à jour le profil (name/email/password) de l'utilisateur authentifié.
     * <p>Régénère un JWT si les credentials changent (email ou mot de passe).</p>
     * @param authenticatedEmail email actuel issu du JWT
     * @param req payload de mise à jour
     * @return profil mis à jour, avec nouveau token si nécessaire (sinon {@code null})
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     * @throws EmailAlreadyInUseException si le nouvel email est déjà utilisé
     */
    public ProfileResponse updateProfile(String authenticatedEmail, UpdateProfileRequest req) {
        User user = repo.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + authenticatedEmail + "' not found"));

        boolean changed = false;
        boolean credentialsChanged = false;


        if (req.name() != null && !req.name().isBlank() && !req.name().equals(user.getName())) {
            user.setName(req.name());
            changed = true;
        }

        if (req.email() != null && !req.email().isBlank() && !req.email().equals(user.getEmail())) {
            if (repo.existsByEmail(req.email())) {
                throw new EmailAlreadyInUseException();
            }
            user.setEmail(req.email());
            changed = true;
            credentialsChanged = true;
        }

        if (req.newPassword() != null && !req.newPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.newPassword()));
            changed = true;
            credentialsChanged = true;
        }

        if (changed) {
            repo.save(user);
        }

        String token = credentialsChanged ? jwtService.generate(user.getEmail()) : null;

        return new ProfileResponse(user.getName(), user.getEmail(), token);
    }
}

