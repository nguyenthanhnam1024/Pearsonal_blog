package com.example.personal_blog.service;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.request.RequestEmailTemplate;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EmailTemplateService {
    void validateEmailRequestedAndValidateRole(HttpServletRequest request, BindingResult result) throws MyValidateException;
    EmailTemplate addEmailTemplate(HttpServletRequest request, RequestEmailTemplate requestEmailTemplate, BindingResult result) throws MyValidateException;
    void updateEmailTemplate(HttpServletRequest request, RequestEmailTemplate requestEmailTemplate, BindingResult result) throws MyValidateException;
    void deleteEmailTemplate(HttpServletRequest request, int emailTemplateId) throws MyValidateException;
    List<RequestEmailTemplate> getAllEmail(HttpServletRequest request) throws MyValidateException;
}
