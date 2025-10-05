package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.SubjectResponse;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/** Service des sujets (liste + indicateur d'abonnement). */
@Service
public class SubjectService {

    private final SubjectRepository subjectRepo;
    private final SubscriptionRepository subscriptionRepo;

    /** @param subjectRepo repo des sujets ; @param subscriptionRepo repo des abonnements */
    public SubjectService(SubjectRepository subjectRepo, SubscriptionRepository subscriptionRepo) {
        this.subjectRepo = subjectRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    /**
     * Lister tous les sujets avec un flag {@code subscribed}.
     * @param userId id utilisateur ; si {@code null} ou {@code < 0}, aucun abonnement n'est considéré
     * @return liste des sujets avec indicateur d'abonnement
     */
    public List<SubjectResponse> findAllWithFlag(Long userId) {
        Set<Long> subscribedIds = subscriptionRepo.findSubjectIdsByUserId(userId);

        return subjectRepo.findAll().stream()
                .map(s -> new SubjectResponse(
                        s.getId(), s.getName(), s.getDescription(),
                        subscribedIds.contains(s.getId())
                ))
                .toList();
    }
}
