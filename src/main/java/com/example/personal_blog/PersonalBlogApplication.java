package com.example.personal_blog;

import com.example.personal_blog.entity.*;
import com.example.personal_blog.repository.AccountRepo;
import com.example.personal_blog.repository.RoleRepo;
import com.example.personal_blog.repository.RoleUserRepo;
import com.example.personal_blog.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@SpringBootApplication
@AllArgsConstructor
public class PersonalBlogApplication {
	private final UserRepo userRepo;
	private final AccountRepo accountRepo;
	private final RoleRepo roleRepo;
	private final RoleUserRepo roleUserRepo;

	public static void main(String[] args) {
		SpringApplication.run(PersonalBlogApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:8080")
						.allowedMethods("POST", "PUT", "GET", "DELETE")
						.allowedHeaders("Authorization", "Content-Type")
						.exposedHeaders("Authorization");
			}
		};
	}

	@Transactional
	@PostConstruct
	public void initialize() {
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		String encodedPassword = bc.encode("admin");
		User user = new User(1L, null, "admin",20, "ayevooi230222@gmail.com", "0123456789", "vietnam");
		userRepo.save(user);
		Account account = new Account(1L, "admin", encodedPassword, user.getUserId());
		accountRepo.save(account);
		Role role = new Role(1, "ADMIN");
		roleRepo.save(role);
		roleRepo.save(new Role(2, "USER"));
		RoleUserId roleUserID = new RoleUserId(1, 1);
		RoleUser roleUser = new RoleUser();
		roleUser.setRoleId(roleUserID.getRoleId());
		roleUser.setUserId(user.getUserId());
		roleUserRepo.save(roleUser);
	}
}
