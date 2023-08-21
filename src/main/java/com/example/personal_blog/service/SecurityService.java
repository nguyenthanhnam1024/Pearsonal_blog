package com.example.personal_blog.service;

import com.example.personal_blog.entity.Account;
import com.example.personal_blog.exception.MyValidateException;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletResponse;

public interface SecurityService {
    void login(HttpServletResponse response, BindingResult bindingResult, Account account) throws MyValidateException;
}
