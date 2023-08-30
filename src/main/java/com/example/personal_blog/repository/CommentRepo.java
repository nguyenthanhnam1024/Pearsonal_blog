package com.example.personal_blog.repository;

import com.example.personal_blog.entity.Comment;
import com.example.personal_blog.entity.CommentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<Comment, CommentId> {
    @Query("select max(c.commentId) from Comment  c where c.userIdOwnPost = :userIdOwnPost and c.postId = :postId")
    Object findMaxCommentIdByUserIdOwnPostAndPostId(@Param("userIdOwnPost") long userIdOwnPost, @Param("postId") long postId);
    List<Comment> findByPostIdAndUserIdOwnPost(long postId, long userIdOwnPost);
    Optional<Comment> findByCommentIdAndPostIdAndUserIdAddComment(long commentId, long postId, long userIdAddComment);
}
