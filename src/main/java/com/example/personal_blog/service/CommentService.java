package com.example.personal_blog.service;

import com.example.personal_blog.entity.Comment;
import com.example.personal_blog.exception.MyValidateException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface CommentService {
    ResponseEntity<Object> createComment(HttpServletRequest request, Comment comment, BindingResult result) throws MyValidateException;
}
