package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<Post> findBySubject_IdOrderByCreatedAtDesc(Long subjectId, Pageable pageable);


}
