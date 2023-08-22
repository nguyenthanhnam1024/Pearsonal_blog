package com.example.personal_blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@IdClass(RoleUserID.class)
public class RoleUser {
    @Id
    private long userID;
    @Id
    private int roleID;
}
