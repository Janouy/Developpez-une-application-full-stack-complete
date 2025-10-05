package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.SubjectResponse;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.SubjectService;
import com.openclassrooms.mddapi.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Endpoints des sujets (liste + subscribe/unsubscribe). */
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService service;
    private final UserRepository userRepo;
    private final SubscriptionService subscriptionService;

    /** @param service service des sujets
     *  @param userRepo repository utilisateur
     *  @param subscriptionService service d'abonnement */
    public SubjectController(SubjectService service, UserRepository userRepo, SubscriptionService subscriptionService) {
        this.service = service;
        this.userRepo = userRepo;
        this.subscriptionService = subscriptionService;
    }

    /**
     * Lister tous les sujets avec un flag d'abonnement pour l'utilisateur courant.
     * @param principal utilisateur authentifié (peut être null → liste publique avec subscribed=false)
     * @return 200 OK + liste des sujets
     */
    @GetMapping
    public ResponseEntity<List<SubjectResponse>> getAll(
            @AuthenticationPrincipal UserDetails principal
    ) {
        if (principal == null) {
            var list = service.findAllWithFlag(-1L);
            return ResponseEntity.ok(list);
        }
        User u = userRepo.findByEmail(principal.getUsername())
                .orElseThrow();
        return ResponseEntity.ok(service.findAllWithFlag(u.getId()));
    }

    /**
     * S'abonner à un sujet.
     * @param subjectId id du sujet (>0)
     * @param principal utilisateur authentifié
     * @return 200 OK + {subjectId, subscribed:true}
     */
    @PostMapping("/{subjectId}/subscribe")
    public ResponseEntity<?> subscribe(
            @PathVariable Long subjectId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        subscriptionService.subscribe(principal.getUsername(), subjectId);
        return ResponseEntity.ok(java.util.Map.of(
                "subjectId", subjectId,
                "subscribed", true
        ));

    }

    /**
     * Se désabonner d'un sujet.
     * @param subjectId id du sujet (>0)
     * @param principal utilisateur authentifié
     * @return 200 OK + {subjectId, subscribed:false}
     */
    @DeleteMapping("/{subjectId}/subscribe")
    public ResponseEntity<?> unsubscribe(
            @PathVariable Long subjectId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        subscriptionService.unsubscribe(principal.getUsername(), subjectId);
        return ResponseEntity.ok(java.util.Map.of(
                "subjectId", subjectId,
                "subscribed", false
        ));
    }

}
