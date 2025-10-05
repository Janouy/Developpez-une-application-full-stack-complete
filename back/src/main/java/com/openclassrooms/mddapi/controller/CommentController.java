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

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getForPost(
            @PathVariable @Positive Long postId,
            @RequestParam(defaultValue = "asc") String order,
            @AuthenticationPrincipal UserDetails principal
    ) {
        boolean newestFirst = "desc".equalsIgnoreCase(order);
        return ResponseEntity.ok(service.getForPost(principal.getUsername(), postId, newestFirst));
    }

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
