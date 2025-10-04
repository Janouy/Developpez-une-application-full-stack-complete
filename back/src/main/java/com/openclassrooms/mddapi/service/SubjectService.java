package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.SubjectResponse;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepo;
    private final SubscriptionRepository subscriptionRepo;

    public SubjectService(SubjectRepository subjectRepo, SubscriptionRepository subscriptionRepo) {
        this.subjectRepo = subjectRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

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
