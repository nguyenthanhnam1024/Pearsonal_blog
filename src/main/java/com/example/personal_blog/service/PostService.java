package com.example.personal_blog.service;

import com.example.personal_blog.entity.Post;
import com.example.personal_blog.exception.MyValidateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface PostService {
    ResponseEntity<Object> createPost(HttpServletRequest request, Post post, BindingResult result) throws MyValidateException;
    ResponseEntity<Page<Post>> getPostOfUserByPageDescending(HttpServletRequest request, Pageable pageable) throws MyValidateException;
    ResponseEntity<Object> deletePost(HttpServletRequest request, Post post) throws MyValidateException;
    ResponseEntity<Page<Post>> findAllLimit10Descending(Pageable pageable) throws MyValidateException;
    ResponseEntity<Object> updatePost(HttpServletRequest request, Post post, BindingResult result) throws MyValidateException;
}
