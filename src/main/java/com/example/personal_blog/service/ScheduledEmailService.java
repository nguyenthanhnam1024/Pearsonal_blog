package com.example.personal_blog.service;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.exception.MyBadRequestEx;

import java.io.IOException;

public interface ScheduledEmailService {
    void sendScheduledEmail();
    void instantSendEmail(EmailTemplate emailTemplate);
    void scheduleAOneTimeEmail();
    void sendAttachmentsFromLocal(EmailTemplate emailTemplate) throws MyBadRequestEx;
    void sendAttachmentsFromUrl(EmailTemplate emailTemplate) throws IOException, MyBadRequestEx;
}
