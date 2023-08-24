package com.example.personal_blog.service;

import com.example.personal_blog.entity.Account;
import com.example.personal_blog.exception.MyValidateException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletResponse;

public interface SecurityService {
    ResponseEntity<Object> login(HttpServletResponse response, BindingResult bindingResult, Account account) throws Exception;
}
