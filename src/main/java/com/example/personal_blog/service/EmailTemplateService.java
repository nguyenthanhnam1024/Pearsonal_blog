package com.example.personal_blog.service;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.exception.MyValidateException;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface EmailTemplateService {
    void validateEmailRequestedAndValidateRole(HttpServletRequest request, EmailTemplate emailTemplate, BindingResult result) throws MyValidateException;
    EmailTemplate addEmailTemplate(HttpServletRequest request, EmailTemplate emailTemplate, BindingResult result) throws MyValidateException;
    void updateEmailTemplate(HttpServletRequest request, EmailTemplate emailTemplate, BindingResult result) throws MyValidateException;
    void deleteEmailTemplate(HttpServletRequest request, EmailTemplate emailTemplate) throws MyValidateException;
}
