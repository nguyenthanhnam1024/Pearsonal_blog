package com.example.personal_blog.service;

import com.example.personal_blog.entity.Posts;
import com.example.personal_blog.exception.MyValidateException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface PostsService {
    ResponseEntity<Object> createPosts(HttpServletRequest request, Posts posts, BindingResult result) throws MyValidateException;
    ResponseEntity<Object> getAllPosts(HttpServletRequest request) throws MyValidateException;
    ResponseEntity<Object> deletePosts(HttpServletRequest request, Posts posts) throws MyValidateException;
}
