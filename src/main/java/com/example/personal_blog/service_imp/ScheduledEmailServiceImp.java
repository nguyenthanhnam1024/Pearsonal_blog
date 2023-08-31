package com.example.personal_blog.service_imp;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.repository.EmailTemplateRepo;
import com.example.personal_blog.repository.UserRepo;
import com.example.personal_blog.service.ScheduledEmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduledEmailServiceImp implements ScheduledEmailService {
    private final JavaMailSender javaMailSender;
    private final UserRepo userRepo;
    private final EmailTemplateRepo emailTemplateRepo;

    @Override
    @Scheduled(fixedRate = 60000)
    public void sendScheduledEmail() {
        List<EmailTemplate> emailTemplates = emailTemplateRepo.findBySendBackAndForth();
        if (!emailTemplates.isEmpty()) {
            for (EmailTemplate template : emailTemplates) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setSubject(template.getTitle());
                message.setText(template.getContent());
                for (String email : userRepo.findEmails()) {
                    message.setTo(email);
                    javaMailSender.send(message);
                }
            }
        }
    }

    @Override
    public void instantSendEmail(EmailTemplate template) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(template.getTitle());
        message.setText(template.getContent());
        for (String email : userRepo.findEmails()) {
            message.setTo(email);
            javaMailSender.send(message);
        }
    }

    @Override
    @Scheduled(fixedRate = 10000*6)
    public void scheduleAOneTimeEmail() {
        List<EmailTemplate> emailTemplates = emailTemplateRepo.findEmailsSendOnce();
        Date currentTime = new Date();
        SimpleMailMessage message = new SimpleMailMessage();
        for (EmailTemplate emailExist : emailTemplates) {
            if (currentTime.after(emailExist.getSendTime())) {
                message.setSubject(emailExist.getTitle());
                message.setText(emailExist.getContent());
                for (String email : userRepo.findEmails()) {
                    message.setTo(email);
                    javaMailSender.send(message);
                }
            }
        }
    }
}
