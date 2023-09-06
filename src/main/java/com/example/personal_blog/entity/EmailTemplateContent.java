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
@IdClass(EmailTemplateContentId.class)
public class EmailTemplateContent {
    @Id
    private int emailTemplateContentId;
    @Id
    private int emailId;
    @NotBlank(message = "category must not other blank")
    private String category;
    @NotBlank(message = "value must not other blank")
    private String value;
}
