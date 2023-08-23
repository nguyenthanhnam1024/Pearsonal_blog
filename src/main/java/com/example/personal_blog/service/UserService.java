package com.example.personal_blog.service;

import com.example.personal_blog.exception.MyValidateException;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    ResponseEntity<Object> getAllUser(HttpServletRequest request) throws MyValidateException;
}
