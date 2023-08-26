package com.example.personal_blog.repository;

import com.example.personal_blog.entity.Posts;
import com.example.personal_blog.entity.PostsID;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PostsRepo extends JpaRepository<Posts, PostsID> {
    Page<Posts> findByUserID(long userId, Pageable pageable);
    Optional<Posts> findByUserIDAndTitleAndContent(long userID, String title, String content);
    @Query("select max(p.postsID) from Posts p where p.userID = :userID")
    Object findPostIDMax(@Param("userID") long userID);
}
