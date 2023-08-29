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
@IdClass(RoleUserId.class)
public class RoleUser {
    @Id
    private long userId;
    @Id
    private int roleId;
}
