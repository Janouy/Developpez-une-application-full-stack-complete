package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CreateCommentRequest;
import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/** Endpoints CRUD pour les commentaires d'un post. */
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService service;

    /** @param service service métier des commentaires */
    public CommentController(CommentService service) {
        this.service = service;
    }

    /**
     * Lister les commentaires d'un post (tri asc/desc).
     * @param postId id du post (> 0)
     * @param order  tri: {@code asc} (défaut) ou {@code desc}
     * @param principal utilisateur authentifié
     * @return 200 OK avec la liste des commentaires
     */
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getForPost(
            @PathVariable @Positive Long postId,
            @RequestParam(defaultValue = "asc") String order,
            @AuthenticationPrincipal UserDetails principal
    ) {
        boolean newestFirst = "desc".equalsIgnoreCase(order);
        return ResponseEntity.ok(service.getForPost(principal.getUsername(), postId, newestFirst));
    }

    /**
     * Créer un commentaire sur un post.
     * @param postId id du post
     * @param principal utilisateur authentifié
     * @param req payload de création (contenu)
     * @return 201 Created avec le commentaire créé
     */
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody CreateCommentRequest req
    ) {
        var resp = service.add(principal.getUsername(), postId, req);
        return ResponseEntity.created(URI.create("/api/posts/" + postId + "/comments/" + resp.id()))
                .body(resp);
    }
}
