package com.example.personal_blog.repository;

import com.example.personal_blog.entity.RoleUser;
import com.example.personal_blog.entity.RoleUserID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleUserRepo extends JpaRepository<RoleUser, RoleUserID> {
    Optional<RoleUser> findByUserID(long userID);
}
