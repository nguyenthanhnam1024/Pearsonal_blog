package com.example.personal_blog.request;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.entity.EmailTemplateContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RequestEmailTemplate {
    @Valid
    private EmailTemplate emailTemplate;
    @Valid
    private List<EmailTemplateContent> listContent;
}
