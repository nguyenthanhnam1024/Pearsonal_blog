package com.example.personal_blog.repository;

import com.example.personal_blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByAccountID(long accountID);
    Optional<User> findByName(String name);
}
