package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Comment;
import org.springframework.data.jpa.repository.*;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<Comment> findByPost_IdOrderByCreatedAtAsc(Long postId);

    @EntityGraph(attributePaths = {"user"})
    List<Comment> findByPost_IdOrderByCreatedAtDesc(Long postId);

}
