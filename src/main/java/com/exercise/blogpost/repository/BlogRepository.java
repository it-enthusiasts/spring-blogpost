package com.exercise.blogpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.exercise.blogpost.model.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {

}
