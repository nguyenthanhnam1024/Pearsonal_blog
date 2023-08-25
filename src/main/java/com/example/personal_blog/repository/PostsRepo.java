package com.example.personal_blog.repository;

import com.example.personal_blog.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepo extends JpaRepository<Posts, Long> {
    List<Posts> findAllByUserID(long userID);
    Optional<Posts> findByUserIDAndTitleAndContent(long userID, String title, String content);
}
