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
@IdClass(CommentID.class)
public class Comment {
    @Id
    private long commentID;
    @Id
    private long userID;
    @Id
    private long postsID;
    @NotBlank(message = "content must other blank")
    private String content;
}
