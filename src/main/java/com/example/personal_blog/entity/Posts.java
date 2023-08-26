package com.example.personal_blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@IdClass(PostsID.class)
public class Posts {
    @Id
    private long postsID;
    @Id
    private long userID;

    @NotBlank(message = "title must other blank")
    private String title;

    @NotBlank(message = "title must other blank")
    private String content;

    private LocalDate postTime;
}
