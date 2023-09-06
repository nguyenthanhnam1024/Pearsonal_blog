package com.example.personal_blog.service_imp;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.entity.EmailTemplateContent;
import com.example.personal_blog.exception.MyBadRequestEx;
import com.example.personal_blog.repository.EmailTemplateContentRepo;
import com.example.personal_blog.repository.EmailTemplateRepo;
import com.example.personal_blog.repository.UserRepo;
import com.example.personal_blog.service.ScheduledEmailService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@EnableScheduling
public class ScheduledEmailServiceImp implements ScheduledEmailService {
    private final JavaMailSender javaMailSender;
    private final UserRepo userRepo;
    private final EmailTemplateRepo emailTemplateRepo;
    private final EmailTemplateContentRepo emailTemplateContentRepo;

    @Override
    public void addAttachmentLocalToBody(MimeMessageHelper helper, String path) throws MessagingException {
        FileSystemResource file = new FileSystemResource(path);
        String fileName = file.getFilename();
        if (fileName != null) {
            helper.addAttachment(fileName, file);
        } else {
            helper.addAttachment(path, file);
        }
    }

    @Override
    public void addAttachmentUrlToBody(MimeMessageHelper helper, String url) throws IOException, MessagingException {
        URL file = new URL(url);
        URLConnection connection = file.openConnection();
        InputStream inputStream = connection.getInputStream();
        ByteArrayResource resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        String fileName = resource.getFilename();
        if (fileName != null) {
            helper.addAttachment(fileName, resource);
        } else {
            helper.addAttachment("url file", resource);
        }
    }

    @Override
    public void setBodyForEmail(MimeMessageHelper helper, int emailTemplateId) throws MyBadRequestEx {
        try {
            for (EmailTemplateContent e : emailTemplateContentRepo.findByEmailId(emailTemplateId)) {
                if (e.getCategory().equals("text")) {
                    helper.setText(e.getValue());
                }
                else if (e.getCategory().equals("path")) {
                    this.addAttachmentLocalToBody(helper, e.getValue());
                }
                else {
                    this.addAttachmentUrlToBody(helper, e.getValue());
                }
            }
        } catch (Exception e) {
            throw new MyBadRequestEx(e.getMessage());
        }
    }

    @Override
    @Scheduled(fixedRate = 10000*12)
    public void sendScheduledEmail() throws MyBadRequestEx {
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

    @Override
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

    @Override
    @Scheduled(fixedRate = 10000*6)
    public void scheduleAOneTimeEmail() throws MyBadRequestEx {
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
