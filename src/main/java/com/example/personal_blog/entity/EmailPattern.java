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
public class EmailPattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int emailPatternId;

    @NotBlank(message = "Title must not be blank")
    private String title;

    private Date sendTime;
    private boolean sent;
    private boolean sendRecurring;
}

