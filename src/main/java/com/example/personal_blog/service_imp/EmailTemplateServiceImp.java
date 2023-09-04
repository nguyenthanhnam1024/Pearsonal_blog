package com.example.personal_blog.service_imp;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.repository.EmailTemplateRepo;
import com.example.personal_blog.service.EmailTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailTemplateServiceImp implements EmailTemplateService {
    private final Commons commons;
    private final EmailTemplateRepo emailTemplateRepo;

    @Override
    public void validateEmailRequestedAndValidateRole(HttpServletRequest request, EmailTemplate emailTemplate, BindingResult result) throws MyValidateException {
        commons.validateForADMIN(request);
        if (result.hasErrors()) {
            throw new MyValidateException("request invalid");
        }
    }

    @Override
    public void addEmailTemplate(HttpServletRequest request, EmailTemplate emailTemplate, BindingResult result) throws MyValidateException {
        this.validateEmailRequestedAndValidateRole(request, emailTemplate, result);
        Optional<EmailTemplate> emailTemplateOptional =
                emailTemplateRepo.findByTitleAndContentAndSendTimeAndSendRecurringAndSent
                        (emailTemplate.getTitle(), emailTemplate.getContent(), emailTemplate.getSendTime(), emailTemplate.isSendRecurring(), emailTemplate.isSent());
        if (emailTemplateOptional.isPresent()) {
            throw new MyValidateException("email have content similar already exist");
        }
        emailTemplateRepo.save(emailTemplate);
    }

    @Override
    public void updateEmailTemplate(HttpServletRequest request, EmailTemplate emailTemplate, BindingResult result) throws MyValidateException {
        this.validateEmailRequestedAndValidateRole(request, emailTemplate, result);
        Optional<EmailTemplate> emailTemplateOptional = emailTemplateRepo.findById(emailTemplate.getEmailTemplateId());
        if (!emailTemplateOptional.isPresent()) {
            throw new MyValidateException("can't find this email in system");
        }
        emailTemplateRepo.save(emailTemplate);
    }

    @Override
    public void deleteEmailTemplate(HttpServletRequest request, EmailTemplate emailTemplate) throws MyValidateException {
        commons.validateForADMIN(request);
        Optional<EmailTemplate> emailTemplateOptional = emailTemplateRepo.findById(emailTemplate.getEmailTemplateId());
        if (emailTemplateOptional.isPresent()) {
            emailTemplateRepo.deleteById(emailTemplate.getEmailTemplateId());
            return;
        }
        throw new MyValidateException("Email delete failed");
    }
}
