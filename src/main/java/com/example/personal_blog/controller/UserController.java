package com.example.personal_blog.controller;

import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/getAll")
    public Object getAllUser(HttpServletRequest request) throws MyValidateException {
        return  userService.getAllUser(request);
    }
}
