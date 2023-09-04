package com.example.personal_blog.service_imp;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.exception.MyBadRequestEx;
import com.example.personal_blog.repository.EmailTemplateRepo;
import com.example.personal_blog.repository.UserRepo;
import com.example.personal_blog.service.ScheduledEmailService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduledEmailServiceImp implements ScheduledEmailService {
    private final JavaMailSender javaMailSender;
    private final UserRepo userRepo;
    private final EmailTemplateRepo emailTemplateRepo;

    @Override
    @Scheduled(fixedRate = 10000)
    public void sendScheduledEmail() {
        List<EmailTemplate> emailTemplates = emailTemplateRepo.findBySenRecurringValue();
        if (!emailTemplates.isEmpty()) {
            SimpleMailMessage message = new SimpleMailMessage();
            String[] mails = userRepo.findEmails().toArray(new String[0]);
            for (EmailTemplate template : emailTemplates) {
                message.setTo(mails);
                message.setSubject(template.getTitle());
                message.setText(template.getContent());
                javaMailSender.send(message);
            }
        }
    }

    @Override
    public void instantSendEmail(EmailTemplate template) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(template.getTitle());
        message.setText(template.getContent());
        message.setTo(userRepo.findEmails().toArray(new String[0]));
        javaMailSender.send(message);
    }

    @Override
    @Scheduled(fixedRate = 10000)
    public void scheduleAOneTimeEmail() {
        List<EmailTemplate> emailTemplates = emailTemplateRepo.findEmailsSendOnce();
        Date currentTime = new Date();
        SimpleMailMessage message = new SimpleMailMessage();
        String[] emails = userRepo.findEmails().toArray(new String[0]);
        for (EmailTemplate emailExist : emailTemplates) {
            if (currentTime.after(emailExist.getSendTime())) {
                message.setSubject(emailExist.getTitle());
                message.setText(emailExist.getContent());
                message.setTo(emails);
                javaMailSender.send(message);
            }
        }
    }

    @Override
    public void sendAttachmentsFromLocal(EmailTemplate emailTemplate) throws MyBadRequestEx {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(userRepo.findEmails().toArray(new String[0]));
            helper.setSubject(emailTemplate.getTitle());
            helper.setText(emailTemplate.getContent());

            FileSystemResource file = new FileSystemResource(emailTemplate.getContent());
            String fileName = file.getFilename();
            if (fileName != null) {
                helper.addAttachment(fileName, file);
            } else {
                helper.addAttachment(emailTemplate.getContent(), file);
            }


            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new MyBadRequestEx(e.getMessage());
        }
        emailTemplate.setSent(true);
        emailTemplateRepo.save(emailTemplate);
    }

    @Override
    public void sendAttachmentsFromUrl(EmailTemplate emailTemplate) throws MyBadRequestEx {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(userRepo.findEmails().toArray(new String[0]));
            helper.setSubject(emailTemplate.getTitle());
            helper.setText(emailTemplate.getContent());

            URL fileUrl = new URL(emailTemplate.getContent());
            URLConnection connection = fileUrl.openConnection();
            InputStream inputStream  = connection.getInputStream();
            ByteArrayResource resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
            String fileName = resource.getFilename();
            if (fileName != null) {
                helper.addAttachment(fileName, resource);
            } else {
                helper.addAttachment("url file", resource);
            }

            javaMailSender.send(mimeMessage);
        } catch (MessagingException | IOException e) {
            throw new MyBadRequestEx(e.getMessage());
        }
        emailTemplate.setSent(true);
        emailTemplateRepo.save(emailTemplate);
    }
}
