package com.example.personal_blog.controller;

import com.example.personal_blog.entity.Posts;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.service.PostsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostsController {
    private final PostsService postsService;

    @PostMapping("/create")
    public ResponseEntity<Object> createPosts(HttpServletRequest request, @RequestBody @Valid Posts posts, BindingResult result) throws MyValidateException {
        return postsService.createPosts(request,posts, result);
    }
}
