package com.example.personal_blog.service_imp;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.entity.EmailTemplateContent;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.repository.EmailTemplateContentRepo;
import com.example.personal_blog.repository.EmailTemplateRepo;
import com.example.personal_blog.request.RequestEmailTemplate;
import com.example.personal_blog.service.EmailTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmailTemplateServiceImp implements EmailTemplateService {
    private final Commons commons;
    private final EmailTemplateRepo emailTemplateRepo;
    private final EmailTemplateContentRepo emailTemplateContentRepo;

    @Override
    public void validateEmailRequestedAndValidateRole(HttpServletRequest request, BindingResult result) throws MyValidateException {
        commons.validateForADMIN(request);
        if (result.hasErrors()) {
            throw new MyValidateException("request invalid");
        }
    }

    @Override
    @Transactional
    public EmailTemplate addEmailTemplate(HttpServletRequest request, RequestEmailTemplate requestEmailTemplate, BindingResult result) throws MyValidateException {
        this.validateEmailRequestedAndValidateRole(request, result);
        EmailTemplate emailTemplate = emailTemplateRepo.save(requestEmailTemplate.getEmailTemplate());
        int emailId = emailTemplate.getEmailTemplateId();
        int contentId = 0;
        for (EmailTemplateContent e : requestEmailTemplate.getListContent()) {
            e.setEmailId(emailId);
            e.setEmailTemplateContentId(contentId++);
        }
        emailTemplateContentRepo.saveAll(requestEmailTemplate.getListContent());
        return emailTemplate;
    }

    @Override
    @Transactional
    public void updateEmailTemplate(HttpServletRequest request, RequestEmailTemplate requestEmailTemplate, BindingResult result) throws MyValidateException {
        this.validateEmailRequestedAndValidateRole(request, result);
        Optional<EmailTemplate> emailTemplateOptional = emailTemplateRepo.findById(requestEmailTemplate.getEmailTemplate().getEmailTemplateId());
        if (!emailTemplateOptional.isPresent()) {
            throw new MyValidateException("can't find this email in system");
        }
        emailTemplateContentRepo.deleteAllByEmailId(requestEmailTemplate.getEmailTemplate().getEmailTemplateId());
        emailTemplateRepo.save(requestEmailTemplate.getEmailTemplate());
        int contentId = 0;
        for (EmailTemplateContent e : requestEmailTemplate.getListContent()) {
            e.setEmailId(emailTemplateOptional.get().getEmailTemplateId());
            e.setEmailTemplateContentId(contentId++);
        }
        emailTemplateContentRepo.saveAll(requestEmailTemplate.getListContent());
    }

    @Override
    @Transactional
    public void deleteEmailTemplate(HttpServletRequest request, int emailTemplateId) throws MyValidateException {
        commons.validateForADMIN(request);
        Optional<EmailTemplate> emailTemplateOptional = emailTemplateRepo.findById(emailTemplateId);
        if (emailTemplateOptional.isPresent()) {
            emailTemplateContentRepo.deleteAllByEmailId(emailTemplateId);
            emailTemplateRepo.deleteById(emailTemplateId);
            return;
        }
        throw new MyValidateException("Email delete failed");
    }

    @Override
    public List<RequestEmailTemplate> getAllEmail(HttpServletRequest request) throws MyValidateException {
        commons.validateForADMIN(request);
        return emailTemplateRepo.findAll().stream()
                .map(e -> new RequestEmailTemplate(e, emailTemplateContentRepo.findByEmailId(e.getEmailTemplateId())))
                .collect(Collectors.toList());
    }
}
