package com.example.personal_blog.service;

import com.example.personal_blog.entity.Posts;
import com.example.personal_blog.exception.MyValidateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface PostsService {
    ResponseEntity<Object> createPosts(HttpServletRequest request, Posts posts, BindingResult result) throws MyValidateException;
    ResponseEntity<Page<Posts>> getPostsOfUserByPageDescending(HttpServletRequest request, Pageable pageable) throws MyValidateException;
    ResponseEntity<Object> deletePosts(HttpServletRequest request, Posts posts) throws MyValidateException;
    ResponseEntity<Page<Posts>> findAllLimit10Descending(Pageable pageable) throws MyValidateException;
    ResponseEntity<Object> updatePosts(HttpServletRequest request, Posts posts, BindingResult result) throws MyValidateException;
}
