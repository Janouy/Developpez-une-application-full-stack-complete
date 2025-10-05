package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CreatePostRequest;
import com.openclassrooms.mddapi.dto.PostResponse;
import com.openclassrooms.mddapi.error.PostNotFoundException;
import com.openclassrooms.mddapi.error.SubjectNotFoundException;
import com.openclassrooms.mddapi.error.UserNotFoundException;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Service des posts (feed, lecture, création). */
@Service
public class PostService {

    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final SubjectRepository subjectRepo;

    /** @param postRepo repo posts ; @param userRepo repo users ; @param subjectRepo repo subjects */
    public PostService(PostRepository postRepo, UserRepository userRepo, SubjectRepository subjectRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.subjectRepo = subjectRepo;
    }

    /**
     * Retourner le feed des posts (subjects suivis), du plus récent au plus ancien.
     * @param authenticatedEmail email de l'utilisateur authentifié
     * @return liste de posts triés décroissant par date
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getFeedForUser(String authenticatedEmail) {
        User u = userRepo.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + authenticatedEmail + "' not found"));

        return postRepo.findBySubject_Subscriptions_User_IdOrderByCreatedAtDesc(u.getId())
                .stream()
                .map(p -> new PostResponse(
                        p.getId(),
                        p.getSubject().getName(),
                        p.getUser().getName(),
                        p.getTitle(),
                        p.getContent(),
                        p.getCreatedAt()
                ))
                .toList();
    }

    /**
     * Retourner un post par son id (règle d'accès selon ta politique).
     * @param authenticatedEmail email de l'utilisateur authentifié
     * @param postId identifiant du post
     * @return post demandé
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     * @throws PostNotFoundException si le post n'existe pas
     */
    @Transactional(readOnly = true)
    public PostResponse getOneForUser(String authenticatedEmail, Long postId) {
        var user = userRepo.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + authenticatedEmail + "' not found"));

        var post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id '" + postId + "' not found"));

        return new PostResponse(
                post.getId(),
                post.getSubject().getName(),
                post.getUser().getName(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt()
        );
    }

    /**
     * Créer un post dans un subject existant (auteur = user connecté).
     * @param authenticatedEmail email de l'utilisateur authentifié
     * @param req payload de création (subjectId, title, content)
     * @return post créé
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     * @throws SubjectNotFoundException si le subject n'existe pas
     */
    @Transactional
    public PostResponse create(String authenticatedEmail, CreatePostRequest req) {
        var user = userRepo.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + authenticatedEmail + "' not found"));

        var subject = subjectRepo.findById(req.subjectId())
                .orElseThrow(() -> new SubjectNotFoundException("Subject with id '" + req.subjectId() + "' not found"));

        var post = new Post();
        post.setUser(user);
        post.setSubject(subject);
        post.setTitle(req.title());
        post.setContent(req.content());

        var saved = postRepo.saveAndFlush(post);

        return new PostResponse(
                saved.getId(),
                saved.getSubject().getName(),
                saved.getUser().getName(),
                saved.getTitle(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }
}
