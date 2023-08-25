package com.example.personal_blog.service;

import com.example.personal_blog.entity.User;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.response.ResponseLogin;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    ResponseEntity<Object> getAllUser(HttpServletRequest request) throws MyValidateException;
    ResponseEntity<Object> updateInfoUser(HttpServletRequest request, User user, BindingResult result) throws MyValidateException;
    ResponseEntity<Object> updateRoleForUserByAdmin(HttpServletRequest request, ResponseLogin responseLogin) throws MyValidateException;
}
