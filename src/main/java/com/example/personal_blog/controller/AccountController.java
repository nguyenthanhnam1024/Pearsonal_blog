package com.example.personal_blog.controller;

import com.example.personal_blog.entity.Account;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.request.RequestUpdateAccount;
import com.example.personal_blog.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account, BindingResult bindingResult) throws MyValidateException {
        return accountService.createAccount(account, bindingResult);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateAccount(@RequestBody @Valid RequestUpdateAccount requestUpdateAccount, BindingResult bindingResult) throws MyValidateException {
        return accountService.updatePassword(requestUpdateAccount, bindingResult);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAccount(HttpServletRequest request, @RequestBody Account account) throws MyValidateException {
        return accountService.deleteAccount(request, account.getPassword());
    }
}
