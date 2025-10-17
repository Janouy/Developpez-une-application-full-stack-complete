package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

/** Adaptateur Spring Security pour charger un utilisateur par email. */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    /** @param repo repository utilisateur */
    public AppUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * Charger un utilisateur par email pour l'authentification.
     * @param login email ou nom de l'utilisateur
     * @return {@link UserDetails} utilisé par Spring Security
     * @throws UsernameNotFoundException si aucun utilisateur n'existe pour cet email
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User u = repo.findByEmail(login).or(()-> repo.findByName(login))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPassword())
                .authorities(Collections.emptyList())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
