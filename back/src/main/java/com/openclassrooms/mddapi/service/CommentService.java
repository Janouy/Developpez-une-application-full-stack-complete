package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CreateCommentRequest;
import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.error.PostNotFoundException;
import com.openclassrooms.mddapi.error.UserNotFoundException;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final SubscriptionRepository subscriptionRepo;

    public CommentService(CommentRepository commentRepo, PostRepository postRepo,
                          UserRepository userRepo, SubscriptionRepository subscriptionRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    @Transactional
    public CommentResponse add(String email, Long postId, CreateCommentRequest req) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + email + "' not found"));

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id '" + postId + "' not found"));

        if (!subscriptionRepo.existsByUser_IdAndSubject_Id(user.getId(), post.getSubject().getId())) {
            throw new AccessDeniedException("Not subscribed to subject " + post.getSubject().getId());
        }

        Comment c = new Comment();
        c.setPost(post);
        c.setUser(user);
        c.setContent(req.content());

        Comment saved = commentRepo.save(c);
        return new CommentResponse(
                saved.getId(),
                post.getId(),
                user.getId(),
                user.getName(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }

    public List<CommentResponse> getForPost(String email, Long postId, boolean newestFirst) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + email + "' not found"));

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id '" + postId + "' not found"));


        if (!subscriptionRepo.existsByUser_IdAndSubject_Id(user.getId(), post.getSubject().getId())) {
            throw new AccessDeniedException("Not subscribed");
        }

        var comments = newestFirst
                ? commentRepo.findByPost_IdOrderByCreatedAtDesc(postId)
                : commentRepo.findByPost_IdOrderByCreatedAtAsc(postId);

        return comments.stream()
                .map(c -> new CommentResponse(
                        c.getId(),
                        postId,
                        c.getUser().getId(),
                        c.getUser().getName(),
                        c.getContent(),
                        c.getCreatedAt()
                ))
                .toList();
    }

}
