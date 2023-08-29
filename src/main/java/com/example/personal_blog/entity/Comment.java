package com.example.personal_blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@IdClass(CommentId.class)
public class Comment {
    @Id
    private long commentId;
    @Id
    private long userIdAddComment;
    @Id
    private long userIdOwnPost;
    @Id
    private long postId;
    @NotBlank(message = "content must other blank")
    private String content;
}
