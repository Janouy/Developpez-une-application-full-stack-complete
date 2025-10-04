package com.openclassrooms.mddapi.repository;


import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
    @Query("select s.subject.id from Subscription s where s.user.id = :userId")
    Set<Long> findSubjectIdsByUserId(Long userId);

    boolean existsByUser_IdAndSubject_Id(Long userId, Long subjectId);
}

