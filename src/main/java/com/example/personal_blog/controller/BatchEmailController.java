package com.example.personal_blog.controller;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.service.EmailTemplateService;
import com.example.personal_blog.service.ScheduledEmailService;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/batchEmail")
@AllArgsConstructor
public class BatchEmailController {
    private final ScheduledEmailService scheduledEmailService;
    private final EmailTemplateService emailTemplateService;

    @PostMapping("/instantSending")
    public String instantSending(HttpServletRequest request, EmailTemplate emailTemplate, BindingResult result) throws MyValidateException {
        emailTemplate.setSendBackAndForth(false);
        scheduledEmailService.instantSendEmail(emailTemplateService.addEmailTemplate(request, emailTemplate, result));
        emailTemplate.setSend(true);
        return "instant send email success";
    }

    @PostMapping("/addEmail")
    public String createEmail(HttpServletRequest request, @RequestBody @Valid EmailTemplate emailTemplate, BindingResult result) throws MyValidateException {
        emailTemplateService.addEmailTemplate(request, emailTemplate, result);
        return "email create success";
    }

    @PutMapping("/updateEmail")
    public String updateEmailTemplate(HttpServletRequest request, EmailTemplate emailTemplate, BindingResult result) throws MyValidateException {
        emailTemplateService.updateEmailTemplate(request, emailTemplate, result);
        return "email update success";
    }

    @DeleteMapping("/deleteEmail")
    public String deleteEmailTemplate(HttpServletRequest request, EmailTemplate emailTemplate) throws MyValidateException {
        emailTemplateService.deleteEmailTemplate(request, emailTemplate);
        return "email delete success";
    }
}
