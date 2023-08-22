package com.example.personal_blog.controller;

import com.example.personal_blog.entity.Account;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.service.SecurityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/security")
@AllArgsConstructor
public class SecurityController {
    private final SecurityService securityService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(HttpServletResponse response, @RequestBody @Valid Account account, BindingResult bindingResult) throws MyValidateException {
        return securityService.login(response, bindingResult, account);
    }
}
