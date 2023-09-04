package com.example.personal_blog.repository;

import com.example.personal_blog.entity.EmailPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailPatternRepo extends JpaRepository<EmailPattern, Integer> {
}
