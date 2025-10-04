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

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService service;
    private final UserRepository userRepo;
    private final SubscriptionService subscriptionService;


    public SubjectController(SubjectService service, UserRepository userRepo, SubscriptionService subscriptionService) {
        this.service = service;
        this.userRepo = userRepo;
        this.subscriptionService = subscriptionService;
    }

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
