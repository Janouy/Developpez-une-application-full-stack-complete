package com.openclassrooms.mddapi.repository;


import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
    List<Subscription> findByUser_Id(Long userId);
    List<Subscription> findBySubject_Id(Long subjectId);
    long countByUser_Id(Long userId);
}

