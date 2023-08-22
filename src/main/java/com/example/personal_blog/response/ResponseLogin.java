package com.example.personal_blog.response;

import lombok.Data;

@Data
public class ResponseLogin {
    private String avatar;
    private String name;
    private int age;
    private String email;
    private String phoneNumber;
    private String address;
    private String roleName;
}
