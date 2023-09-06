package com.example.personal_blog.repository;

import com.example.personal_blog.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailTemplateRepo extends JpaRepository<EmailTemplate, Integer> {
    @Query("select e from EmailTemplate e where e.sendRecurring = true")
    List<EmailTemplate> findBySenRecurringValue();

    @Query("select e from EmailTemplate e where e.sent = false and e.sendRecurring = false")
    List<EmailTemplate> findEmailsSendOnce();
}
