package com.example.personal_blog.controller;

import com.example.personal_blog.entity.EmailTemplate;
import com.example.personal_blog.exception.MyBadRequestEx;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.request.RequestEmailTemplate;
import com.example.personal_blog.schedule.ScheduleEmails;
import com.example.personal_blog.service.EmailTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/batchEmail")
@AllArgsConstructor
public class BatchEmailController {
    private final EmailTemplateService emailTemplateService;
    private final ScheduleEmails scheduleEmails;

    @PostMapping("/instantSending")
    public String instantSending(HttpServletRequest request, @RequestBody @Valid RequestEmailTemplate requestEmailTemplate, BindingResult result) throws MyValidateException, MyBadRequestEx {
        EmailTemplate emailTemplate = emailTemplateService.addEmailTemplate(request, requestEmailTemplate, result);
        scheduleEmails.instantSendEmail(emailTemplate);
        return "instant send email success";
    }

    @PostMapping("/addEmail")
    public String createEmail(HttpServletRequest request, @RequestBody @Valid RequestEmailTemplate requestEmailTemplate, BindingResult result) throws MyValidateException {
        emailTemplateService.addEmailTemplate(request, requestEmailTemplate, result);
        return "email create success";
    }

    @PutMapping("/updateEmail")
    public String updateEmailTemplate(HttpServletRequest request, @RequestBody @Valid RequestEmailTemplate requestEmailTemplate, BindingResult result) throws MyValidateException {
        emailTemplateService.updateEmailTemplate(request, requestEmailTemplate, result);
        return "email update success";
    }

    @DeleteMapping("/deleteEmail/{emailTemplateId}")
    public String deleteEmailTemplate(HttpServletRequest request, @PathVariable int emailTemplateId) throws MyValidateException {
        emailTemplateService.deleteEmailTemplate(request, emailTemplateId);
        return "email delete success";
    }

    @GetMapping("getAll")
    public List<RequestEmailTemplate> getAll(HttpServletRequest request) throws MyValidateException {
        return emailTemplateService.getAllEmail(request);
    }
}
