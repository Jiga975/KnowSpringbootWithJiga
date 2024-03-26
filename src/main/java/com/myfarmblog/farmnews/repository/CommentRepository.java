package com.myfarmblog.farmnews.repository;

import com.myfarmblog.farmnews.entity.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment>findByPostId(long postId);
}
