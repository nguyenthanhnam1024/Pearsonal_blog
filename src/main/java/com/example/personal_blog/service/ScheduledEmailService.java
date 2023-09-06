package com.example.personal_blog.service;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.exception.MyBadRequestEx;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import java.io.IOException;

public interface ScheduledEmailService {
    void addAttachmentLocalToBody(MimeMessageHelper helper, String path) throws MessagingException;
    void addAttachmentUrlToBody(MimeMessageHelper helper, String url) throws IOException, MessagingException;
    void setBodyForEmail(MimeMessageHelper helper, int emailTemplateId) throws MessagingException, MyBadRequestEx;
    void sendScheduledEmail() throws MyBadRequestEx;
    void instantSendEmail(EmailTemplate emailTemplate) throws MyBadRequestEx;
    void scheduleAOneTimeEmail() throws MyBadRequestEx;
}
