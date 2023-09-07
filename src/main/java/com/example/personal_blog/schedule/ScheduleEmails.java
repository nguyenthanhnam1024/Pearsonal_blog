package com.example.personal_blog.schedule;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.entity.EmailTemplateContent;
import com.example.personal_blog.exception.MyBadRequestEx;
import com.example.personal_blog.repository.EmailTemplateContentRepo;
import com.example.personal_blog.repository.EmailTemplateRepo;
import com.example.personal_blog.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;


@Component
@AllArgsConstructor
@EnableScheduling
public class ScheduleEmails {
    private final JavaMailSender javaMailSender;
    private final UserRepo userRepo;
    private final EmailTemplateRepo emailTemplateRepo;
    private final EmailTemplateContentRepo emailTemplateContentRepo;

    private void addAttachmentLocalToBody(MimeMessageHelper helper, String path) throws MessagingException {
        FileSystemResource file = new FileSystemResource(path);
        String fileName = file.getFilename();
        if (fileName != null) {
            helper.addAttachment(fileName, file);
        } else {
            helper.addAttachment(path, file);
        }
    }

    private void setBodyForEmail(MimeMessageHelper helper, int emailTemplateId) throws MyBadRequestEx {
        try {
            for (EmailTemplateContent e : emailTemplateContentRepo.findByEmailId(emailTemplateId)) {
                if (e.getCategory().equals("text")) {
                    helper.setText(e.getValue());
                } else {
                    this.addAttachmentLocalToBody(helper, e.getValue());
                }
            }
        } catch (Exception e) {
            throw new MyBadRequestEx(e.getMessage());
        }
    }

    @Scheduled(fixedRate = 3600000*24*7)

    private void sendScheduledEmail() throws MyBadRequestEx {
        List<EmailTemplate> emailTemplates = emailTemplateRepo.findBySenRecurringValue();
        try {
            if (!emailTemplates.isEmpty()) {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                String[] mails = userRepo.findEmails().toArray(new String[0]);

                for (EmailTemplate template : emailTemplates) {
                    helper.setTo(mails);
                    helper.setSubject(template.getTitle());
                    this.setBodyForEmail(helper, template.getEmailTemplateId());

                    javaMailSender.send(mimeMessage);
                    template.setSent(true);
                    try {
                        emailTemplateRepo.save(template);
                    } catch (Exception e) {
                        throw new MyBadRequestEx(e.getMessage());
                    }
                }
            }
        } catch (MessagingException e) {
            throw new MyBadRequestEx(e.getMessage());
        }
    }

    public void instantSendEmail(EmailTemplate template) throws MyBadRequestEx {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(userRepo.findEmails().toArray(new String[0]));
            helper.setSubject(template.getTitle());
            this.setBodyForEmail(helper, template.getEmailTemplateId());
            javaMailSender.send(mimeMessage);
            template.setSent(true);
            try {
                emailTemplateRepo.save(template);
            } catch (Exception e) {
                throw new MyBadRequestEx("error saving email template");
            }
        } catch (MessagingException e) {
            throw new MyBadRequestEx(e.getMessage());
        }
    }

    @Scheduled(fixedRate = 10000*6)
    private void scheduleAOneTimeEmail() throws MyBadRequestEx {
        List<EmailTemplate> emailTemplates = emailTemplateRepo.findEmailsSendOnce();
        try {
            if (!emailTemplates.isEmpty()) {
                Date currentTime = new Date();
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                String[] mails = userRepo.findEmails().toArray(new String[0]);

                for (EmailTemplate template : emailTemplates) {
                    if (currentTime.after(template.getSendTime())) {
                        helper.setTo(mails);
                        helper.setSubject(template.getTitle());
                        this.setBodyForEmail(helper, template.getEmailTemplateId());
                        javaMailSender.send(mimeMessage);
                        template.setSent(true);
                        try {
                            emailTemplateRepo.save(template);
                        } catch (Exception e) {
                            throw new MyBadRequestEx("error saving email template");
                        }
                    }

                }
            }
        } catch (MessagingException e) {
            throw new MyBadRequestEx(e.getMessage());
        }
    }
}
