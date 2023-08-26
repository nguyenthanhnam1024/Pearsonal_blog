package com.example.personal_blog.controller;

import com.example.personal_blog.entity.Posts;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.service.PostsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getAll/{page}")
    public ResponseEntity<Page<Posts>> getPostsOfUserByPageDescending(HttpServletRequest request, @PathVariable int page) throws MyValidateException {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "postsID");
        return postsService.getPostsOfUserByPageDescending(request, pageable);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deletePosts(HttpServletRequest request, @RequestBody Posts posts) throws MyValidateException {
        return postsService.deletePosts(request, posts);
    }
}
