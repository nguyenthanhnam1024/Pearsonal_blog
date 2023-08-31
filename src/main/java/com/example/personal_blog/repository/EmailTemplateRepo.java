package com.example.personal_blog.repository;

import com.example.personal_blog.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailTemplateRepo extends JpaRepository<EmailTemplate, Integer> {
    @Query("select e from EmailTemplate e where e.sendBackAndForth = true")
    List<EmailTemplate> findBySendBackAndForth();

    Optional<EmailTemplate> findByTitleAndContent(String title, String content);

    @Query("select e from EmailTemplate e where e.send = false and e.sendBackAndForth = false")
    List<EmailTemplate> findEmailsSendOnce();
}
