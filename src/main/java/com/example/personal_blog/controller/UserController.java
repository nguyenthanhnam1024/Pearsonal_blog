package com.example.personal_blog.controller;

import com.example.personal_blog.entity.User;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.response.ResponseLogin;
import com.example.personal_blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllUser(HttpServletRequest request) throws MyValidateException {
        return  userService.getAllUser(request);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(HttpServletRequest request, @RequestBody @Valid User user, BindingResult result) throws MyValidateException {
        return userService.updateInfoUser(request, user, result);
    }

    @PutMapping("/updateByAdmin")
    public ResponseEntity<Object> updateByAdmin(HttpServletRequest request,@RequestBody ResponseLogin responseLogin) throws MyValidateException {
        return userService.updateRoleForUserByAdmin(request, responseLogin);
    }
}
