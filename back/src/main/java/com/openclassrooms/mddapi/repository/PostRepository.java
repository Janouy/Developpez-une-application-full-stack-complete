package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"subject", "user"})
    List<Post> findBySubject_Subscriptions_User_IdOrderByCreatedAtDesc(Long userId);

    @EntityGraph(attributePaths = {"subject", "user"})
    Optional<Post> findById(Long id);

}
