package com.example.personal_blog.repository;

import com.example.personal_blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findUserByPhoneNumber(String phone);

    @Query("select u.email from User u")
    List<String> findEmails();
}
