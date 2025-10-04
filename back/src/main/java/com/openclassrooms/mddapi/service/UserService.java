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

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder,JwtService jwtService) {
        this.repo = repo; this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse getByEmail(String email) {
        User u = repo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + email + "' not found"));

        return new UserResponse(u.getName(), u.getEmail());
    }
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

