package com.example.personal_blog.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RequestUpdateAccount {
    @NotBlank(message = "fullName user wrong")
    private String fullNameUser;
    @NotBlank(message = "userName wrong")
    private String userName;
    @Size(min = 6, max = 50, message = "new password must from 6 to 50 keyword")
    private String newPassword;
    @NotBlank(message = "old password wrong")
    private String oldPassword;
}
