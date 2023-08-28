package com.example.personal_blog.repository;

import com.example.personal_blog.entity.Comment;
import com.example.personal_blog.entity.CommentID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Comment, CommentID> {
    @Query("select max(c.commentID) from Comment  c where c.userID = :userID and c.postsID = :postsID")
    Object findMaxCommentIDByUserIDAndPostsID(@Param("userID") long userID, @Param("postsID") long postsID);
}
