package com.example.personal_blog.controller;

import com.example.personal_blog.entity.Comment;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.service.CommentService;
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
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<Object> createComment(HttpServletRequest request, @RequestBody @Valid Comment comment, BindingResult result) throws MyValidateException {
        return commentService.createComment(request, comment, result);
    }
}
