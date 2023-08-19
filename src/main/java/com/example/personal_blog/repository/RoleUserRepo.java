package com.example.personal_blog.repository;

import com.example.personal_blog.entity.RoleUserID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleUserRepo extends JpaRepository<RoleUserRepo, RoleUserID> {
}
