//package com.example.personal_blog.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//
//import javax.validation.Valid;
//import javax.validation.constraints.NotBlank;
//import java.util.Date;
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@RequiredArgsConstructor
//public class EmailPatternDTO {
//    private int emailPatternId;
//    @NotBlank(message = "Title must not be blank")
//    private String title;
//    @Valid
//    private List<ContentEmailPattern> contents;
//    private Date sendTime;
//    private boolean sent;
//    private boolean sendRecurring;
//}
