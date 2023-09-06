package com.example.personal_blog.repository;

import com.example.personal_blog.entity.EmailTemplateContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailTemplateContentRepo extends JpaRepository<EmailTemplateContent, Integer> {
    void deleteAllByEmailId(int emailTemplateId);
    List<EmailTemplateContent> findByEmailId(int emailTemplateId);
}
