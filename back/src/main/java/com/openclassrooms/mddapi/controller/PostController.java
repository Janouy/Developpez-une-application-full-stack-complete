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

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getMyFeed(@AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(postService.getFeedForUser(principal.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(
            @PathVariable @Positive Long id,
            @AuthenticationPrincipal UserDetails principal
    ) {
        return ResponseEntity.ok(postService.getOneForUser(principal.getUsername(), id));
    }

    @PostMapping()
    public ResponseEntity<PostResponse> create(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody CreatePostRequest req
    ) {
        var resp = postService.create(principal.getUsername(), req);
        return ResponseEntity.created(java.net.URI.create("/api/posts/" + resp.id())).body(resp);
    }


}
