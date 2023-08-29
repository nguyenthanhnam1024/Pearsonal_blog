package com.example.personal_blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentId implements Serializable {
    private long commentId;
    private long userIdAddComment;
    private long userIdOwnComment;
    private long postId;
}
