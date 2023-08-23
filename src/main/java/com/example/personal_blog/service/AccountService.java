package com.example.personal_blog.service;

import com.example.personal_blog.entity.Account;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.request.RequestUpdateAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface AccountService {
    ResponseEntity<Object> createAccount(Account account, BindingResult bindingResult) throws MyValidateException;
    ResponseEntity<Object> updatePassword(RequestUpdateAccount requestUpdateAccount, BindingResult bindingResult) throws MyValidateException;
    ResponseEntity<Object> deleteAccount(HttpServletRequest request, String password) throws MyValidateException;
}
