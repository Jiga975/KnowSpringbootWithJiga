package com.myfarmblog.farmnews.repository;

import com.myfarmblog.farmnews.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}