package com.example.personal_blog.repository;

import com.example.personal_blog.entity.RoleUser;
import com.example.personal_blog.entity.RoleUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleUserRepo extends JpaRepository<RoleUser, RoleUserId> {
    Optional<RoleUser> findByUserId(long userID);
    void deleteRoleUserByUserId(long userID);
}
