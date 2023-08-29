package com.example.personal_blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@IdClass(PostId.class)
public class Post {
    @Id
    private long postId;
    @Id
    private long userId;

    @NotBlank(message = "title must other blank")
    private String title;

    @NotBlank(message = "title must other blank")
    private String content;

    private Date postTime;
}
