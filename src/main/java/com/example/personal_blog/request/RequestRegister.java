package com.example.personal_blog.request;

import com.example.personal_blog.entity.Account;
import com.example.personal_blog.entity.User;
import lombok.Data;

import javax.validation.Valid;

@Data
public class RequestRegister {
    @Valid
    private User user;
    @Valid
    private Account account;
}
