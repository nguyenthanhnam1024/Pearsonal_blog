package com.example.personal_blog.repository;

import com.example.personal_blog.entity.Post;
import com.example.personal_blog.entity.PostId;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface PostsRepo extends JpaRepository<Post, PostId> {
    Page<Post> findByUserId(long userId, Pageable pageable);
    Optional<Post> findByUserIdAndTitleAndContent(long userId, String title, String content);
    @Query("select max(p.postId) from Post p where p.userId = :userId")
    Object findPostIDMax(@Param("userId") long userId);
    Optional<Post> findByUserIdAndPostId(long userID, long postsID);
}
