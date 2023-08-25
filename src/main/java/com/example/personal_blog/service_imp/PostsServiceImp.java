package com.example.personal_blog.service_imp;

import com.example.personal_blog.entity.Posts;
import com.example.personal_blog.entity.User;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.jwt.ExtractDataFromJwt;
import com.example.personal_blog.repository.PostsRepo;
import com.example.personal_blog.repository.UserRepo;
import com.example.personal_blog.service.PostsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostsServiceImp implements PostsService {
    private final PostsRepo postsRepo;
    private  final ExtractDataFromJwt extractDataFromJwt;
    private final Commons commons;
    private final UserRepo userRepo;

    @Override
    public ResponseEntity<Object> createPosts(HttpServletRequest request, Posts posts, BindingResult result) throws MyValidateException {
        Map<String, String> mapError = commons.handlesBindingResult(result);
        if (!mapError.isEmpty()) {
            return ResponseEntity.badRequest().body(mapError);
        }
        Map<String, Object> infoUser =  extractDataFromJwt.extractInfoUser(request);
        Integer userTypeInteger = (Integer) infoUser.get("userID");
        Optional<User> userOptional = userRepo.findById((long) userTypeInteger);
        if (userOptional.isPresent()) {
            Optional<Posts> postsOptional = postsRepo.findByUserIDAndTitleAndContent(userOptional.get().getUserID(), posts.getTitle(), posts.getContent());
            if (postsOptional.isPresent()) {
                throw new MyValidateException("You already have a similar post");
            }
            try {
                posts.setUserID(userOptional.get().getUserID());
                posts.setPostTime(LocalDate.now());
                postsRepo.save(posts);
                return ResponseEntity.ok("create posts success");
            } catch (Exception e) {
                throw new MyValidateException("error query");
            }
        }
        throw new MyValidateException("authentication error");
    }

    @Override
    public ResponseEntity<Object> getAllPosts(HttpServletRequest request) throws MyValidateException {
        Map<String, Object> infoUser =  extractDataFromJwt.extractInfoUser(request);
        Integer userTypeInteger = (Integer) infoUser.get("userID");
        Optional<User> userOptional = userRepo.findById((long) userTypeInteger);
        if (userOptional.isPresent()) {
            try {
                return ResponseEntity.ok(postsRepo.findAllByUserID((long) userTypeInteger));
            } catch (Exception e) {
                throw new MyValidateException("error query");
            }
        }
        throw new MyValidateException("authentication error");
    }

    @Override
    public ResponseEntity<Object> deletePosts(HttpServletRequest request, Posts posts) throws MyValidateException {
        Map<String, Object> infoUser = extractDataFromJwt.extractInfoUser(request);
        Integer userIDTypeInteger = (Integer) infoUser.get("userID");
        Optional<User> userOptional = userRepo.findById((long) userIDTypeInteger);
        if (userOptional.isPresent()) {
            if (userOptional.get().getUserID() == posts.getUserID()) {
                Optional<Posts> postsOptional = postsRepo.findByUserIDAndTitleAndContent((long) userIDTypeInteger, posts.getTitle(), posts.getContent());
                if (postsOptional.isPresent()) {
                    try {
                        postsRepo.delete(posts);
                        return ResponseEntity.ok("posts delete success");
                    } catch (Exception e) {
                        throw new MyValidateException("error query");
                    }
                }
                throw new MyValidateException("Couldn't find your post to delete");
            }
            throw new MyValidateException("authentication failed");
        }
        throw new MyValidateException("authentication error");
    }
}
