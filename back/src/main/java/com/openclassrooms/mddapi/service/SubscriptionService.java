package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.error.AlreadySubscribedException;
import com.openclassrooms.mddapi.error.SubjectNotFoundException;
import com.openclassrooms.mddapi.error.UserNotFoundException;
import com.openclassrooms.mddapi.model.*;
import com.openclassrooms.mddapi.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriptionService {

    private final UserRepository userRepo;
    private final SubjectRepository subjectRepo;
    private final SubscriptionRepository subscriptionRepo;

    public SubscriptionService(UserRepository userRepo, SubjectRepository subjectRepo, SubscriptionRepository subscriptionRepo) {
        this.userRepo = userRepo;
        this.subjectRepo = subjectRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

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

    @Transactional
    public void unsubscribe(String authenticatedEmail, Long subjectId) {
        var user = userRepo.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + authenticatedEmail + "' not found"));

        subjectRepo.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("Subject with id '" + subjectId + "' not found"));

        subscriptionRepo.deleteById(new SubscriptionId(user.getId(), subjectId));
    }

}
