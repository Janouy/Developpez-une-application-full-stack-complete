package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CreatePostRequest;
import com.openclassrooms.mddapi.dto.PostResponse;
import com.openclassrooms.mddapi.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Endpoints des posts (feed, lecture, création). */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    /** @param postService service métier des posts */
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Récupérer le feed des posts (subjects suivis), du plus récent au plus ancien.
     * @param principal utilisateur authentifié (JWT)
     * @return 200 OK + liste de posts
     */
    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getMyFeed(@AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(postService.getFeedForUser(principal.getUsername()));
    }

    /**
     * Récupérer un post par son id (si abonné au subject du post).
     * @param id id du post (> 0)
     * @param principal utilisateur authentifié
     * @return 200 OK + post ; 403 si non abonné ; 404 si introuvable
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(
            @PathVariable @Positive Long id,
            @AuthenticationPrincipal UserDetails principal
    ) {
        return ResponseEntity.ok(postService.getOneForUser(principal.getUsername(), id));
    }

    /**
     * Créer un post dans un subject existant (auteur = user connecté).
     * @param principal utilisateur authentifié
     * @param req payload de création (subjectId, title, content)
     * @return 201 Created + post créé (Location: /api/posts/{id})
     */
    @PostMapping()
    public ResponseEntity<PostResponse> create(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody CreatePostRequest req
    ) {
        var resp = postService.create(principal.getUsername(), req);
        return ResponseEntity.created(java.net.URI.create("/api/posts/" + resp.id())).body(resp);
    }


}
