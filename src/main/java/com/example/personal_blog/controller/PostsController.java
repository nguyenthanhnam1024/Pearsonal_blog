package com.example.personal_blog.controller;

import com.example.personal_blog.entity.Post;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.service.PostService;
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
@RequestMapping("/post")
@AllArgsConstructor
public class PostsController {
    private final PostService postsService;

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(HttpServletRequest request, @RequestBody @Valid Post post, BindingResult result) throws MyValidateException {
        return postsService.createPost(request,post, result);
    }

    @GetMapping("/findPostsOfUser/{page}")
    public ResponseEntity<Page<Post>> getPostOfUserByPageDescending(HttpServletRequest request, @PathVariable int page) throws MyValidateException {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "postId");
        return postsService.getPostOfUserByPageDescending(request, pageable);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deletePost(HttpServletRequest request, @RequestBody Post post) throws MyValidateException {
        return postsService.deletePost(request, post);
    }

    @GetMapping("/findPostsOfUsers/{page}")
    public ResponseEntity<Page<Post>> findPostOfUsers(@PathVariable int page) throws MyValidateException {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "postTime");
        return postsService.findAllLimit10Descending(pageable);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updatePost(HttpServletRequest request,@RequestBody @Valid Post post, BindingResult result) throws MyValidateException {
        return postsService.updatePost(request, post, result);
    }
}
