package com.example.personal_blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailTemplateContentId implements Serializable {
    private int emailTemplateContentId;
    private int emailId;
}
