package com.example.personal_blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;
    private String avatar;
    @NotBlank(message = "fullName of user must other blank")
    private String name;
    @Min(value = 7, message = "encourage 7+")
    private byte age;
    @Email(message = "email invalid")
    private String email;
    @Size(min = 10,max = 10, message = "phone number invalid")
    private String phoneNumber;
    private String address;
    @Min(value = 1, message = "account notfound")
    private long accountID;
}
