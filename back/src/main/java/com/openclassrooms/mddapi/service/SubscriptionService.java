package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.error.AlreadySubscribedException;
import com.openclassrooms.mddapi.error.SubjectNotFoundException;
import com.openclassrooms.mddapi.error.UserNotFoundException;
import com.openclassrooms.mddapi.model.*;
import com.openclassrooms.mddapi.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service des abonnements (subscribe / unsubscribe). */
@Service
public class SubscriptionService {

    private final UserRepository userRepo;
    private final SubjectRepository subjectRepo;
    private final SubscriptionRepository subscriptionRepo;

    /** @param userRepo repo utilisateurs ; @param subjectRepo repo sujets ; @param subscriptionRepo repo abonnements */
    public SubscriptionService(UserRepository userRepo, SubjectRepository subjectRepo, SubscriptionRepository subscriptionRepo) {
        this.userRepo = userRepo;
        this.subjectRepo = subjectRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    /**
     * Abonner l'utilisateur au sujet.
     * @param authenticatedEmail email de l'utilisateur authentifié
     * @param subjectId id du sujet
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     * @throws SubjectNotFoundException si le sujet n'existe pas
     * @throws AlreadySubscribedException si l'utilisateur est déjà abonné
     */
    @Transactional
    public void subscribe(String authenticatedEmail, Long subjectId) {
        var user = userRepo.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + authenticatedEmail + "' not found"));

        var subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("Subject with id '" + subjectId + "' not found"));

        if (subscriptionRepo.existsByUser_IdAndSubject_Id(user.getId(), subjectId)) {
            throw new AlreadySubscribedException("Already subscribed to subject " + subjectId);
        }

        var sub = new Subscription();
        sub.setId(new SubscriptionId(user.getId(), subjectId));
        sub.setUser(user);
        sub.setSubject(subject);

        subscriptionRepo.save(sub);
    }

    /**
     * Désabonner l'utilisateur du sujet (idempotent).
     * @param authenticatedEmail email de l'utilisateur authentifié
     * @param subjectId id du sujet
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     * @throws SubjectNotFoundException si le sujet n'existe pas
     */
    @Transactional
    public void unsubscribe(String authenticatedEmail, Long subjectId) {
        var user = userRepo.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + authenticatedEmail + "' not found"));

        subjectRepo.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("Subject with id '" + subjectId + "' not found"));

        subscriptionRepo.deleteById(new SubscriptionId(user.getId(), subjectId));
    }

}
