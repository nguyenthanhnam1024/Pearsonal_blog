package com.example.personal_blog.controller;

//import com.example.personal_blog.dto.EmailPatternDTO;
import com.example.personal_blog.entity.EmailPattern;
import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.exception.MyBadRequestEx;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.repository.EmailPatternRepo;
//import com.example.personal_blog.service.EmailPatternService;
import com.example.personal_blog.service.EmailTemplateService;
import com.example.personal_blog.service.ScheduledEmailService;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/batchEmail")
@AllArgsConstructor
public class BatchEmailController {
    private final ScheduledEmailService scheduledEmailService;
    private final EmailTemplateService emailTemplateService;
    private final EmailPatternRepo emailPatternRepo;
//    private final EmailPatternService emailPatternService;

    @PostMapping("/instantSending")
    public String instantSending(HttpServletRequest request,@RequestBody @Valid EmailTemplate emailTemplate, BindingResult result) throws MyValidateException {
        emailTemplateService.validateEmailRequestedAndValidateRole(request, emailTemplate, result);
        scheduledEmailService.instantSendEmail(emailTemplate);
        return "instant send email success";
    }

    @PostMapping("/addEmail")
    public String createEmail(HttpServletRequest request, @RequestBody @Valid EmailTemplate emailTemplate, BindingResult result) throws MyValidateException {
        emailTemplateService.addEmailTemplate(request, emailTemplate, result);
        return "email create success";
    }

    @PutMapping("/updateEmail")
    public String updateEmailTemplate(HttpServletRequest request, @RequestBody @Valid EmailTemplate emailTemplate, BindingResult result) throws MyValidateException {
        emailTemplateService.updateEmailTemplate(request, emailTemplate, result);
        return "email update success";
    }

    @DeleteMapping("/deleteEmail")
    public String deleteEmailTemplate(HttpServletRequest request, EmailTemplate emailTemplate) throws MyValidateException {
        emailTemplateService.deleteEmailTemplate(request, emailTemplate);
        return "email delete success";
    }

    @PostMapping("/instantSendfileFromLocal")
    public String sendAttachmentsFromLocal(HttpServletRequest request, @RequestBody @Valid EmailTemplate emailTemplate, BindingResult result) throws MyValidateException, MyBadRequestEx {
//        emailTemplateService.validateEmailRequestedAndValidateRole(request, emailTemplate, result);
        scheduledEmailService.sendAttachmentsFromLocal(emailTemplate);
        return "file local instant send success";
    }

    @PostMapping("/instantSendFromUrl")
    public String sendAttachmentsFromUrl(HttpServletRequest request, @RequestBody @Valid EmailTemplate emailTemplate, BindingResult result) throws MyValidateException, IOException, MyBadRequestEx {
        emailTemplateService.validateEmailRequestedAndValidateRole(request, emailTemplate, result);
        scheduledEmailService.sendAttachmentsFromUrl(emailTemplate);
        return "file url instant send success";
    }

//    @PostMapping("/addEmailPattern")
//    public EmailPattern createEmailPattern(HttpServletRequest request, @RequestBody EmailPatternDTO emailPatternDTO, BindingResult result) throws MyValidateException {
//        EmailPattern emailPattern1 = (EmailPattern) emailPatternService.saveEmailPattern(request, emailPatternDTO, result);
//        return emailPattern1;
//    }
}
