package com.example.personal_blog.service;

import com.example.personal_blog.entity.EmailTemplate;

public interface ScheduledEmailService {
    void sendScheduledEmail();
    void instantSendEmail(EmailTemplate emailTemplate);
    void scheduleAOneTimeEmail();
}
