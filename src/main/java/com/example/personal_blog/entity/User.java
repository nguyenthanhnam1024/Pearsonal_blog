package com.example.personal_blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

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
    @Max(value = 120, message = "max age equals 120")
    private int age;
    @Email(message = "email invalid")
    @NotBlank(message = "email of user must other blank")
    private String email;
    @Size(min = 10,max = 10, message = "phone number invalid")
    private String phoneNumber;
    private String address;
    private long accountID;
}
