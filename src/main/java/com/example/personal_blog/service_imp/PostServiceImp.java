package com.example.personal_blog.service_imp;

import com.example.personal_blog.entity.Post;
import com.example.personal_blog.entity.User;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.jwt.ExtractDataFromJwt;
import com.example.personal_blog.repository.PostsRepo;
import com.example.personal_blog.repository.UserRepo;
import com.example.personal_blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostServiceImp implements PostService {
    private final PostsRepo postsRepo;
    private  final ExtractDataFromJwt extractDataFromJwt;
    private final Commons commons;
    private final UserRepo userRepo;

    @Override
    public ResponseEntity<Object> createPost(HttpServletRequest request, Post post, BindingResult result) throws MyValidateException {
        Map<String, String> mapError = commons.handlesBindingResult(result);
        if (!mapError.isEmpty()) {
            return ResponseEntity.badRequest().body(mapError);
        }
        long userIdRequest = extractDataFromJwt.getUserId(request);
        Optional<User> userOptional = userRepo.findById(userIdRequest);
        if (userOptional.isPresent()) {
            Optional<Post> postsOptional = postsRepo.findByUserIdAndTitleAndContent(userIdRequest, post.getTitle(), post.getContent());
            if (postsOptional.isPresent()) {
                throw new MyValidateException("You already have a similar post");
            }
            try {
                Object postIDMax = postsRepo.findPostIDMax(userIdRequest);
                if (postIDMax == null) {
                    post.setPostId(1);
                } else {
                    post.setPostId((long) postIDMax + 1);
                }
                post.setUserId(userIdRequest);
                post.setPostTime(new Date());
                postsRepo.save(post);
                return ResponseEntity.ok("create posts success");
            } catch (Exception e) {
                throw new MyValidateException("error query"+e.getMessage());
            }
        }
        throw new MyValidateException("authentication error");
    }

    @Override
    public ResponseEntity<Page<Post>> getPostOfUserByPageDescending(HttpServletRequest request, Pageable pageable) throws MyValidateException {
        long userIdRequest = extractDataFromJwt.getUserId(request);
        Optional<User> userOptional = userRepo.findById(userIdRequest);
        if (userOptional.isPresent()) {
            try {
                return ResponseEntity.ok(postsRepo.findByUserId(userIdRequest, pageable));
            } catch (Exception e) {
                throw new MyValidateException("error query");
            }
        }
        throw new MyValidateException("authentication error");
    }

    @Override
    public ResponseEntity<Object> deletePost(HttpServletRequest request, Post post) throws MyValidateException {
        long userIdRequest = extractDataFromJwt.getUserId(request);
        Optional<User> userOptional = userRepo.findById(userIdRequest);
        if (userOptional.isPresent()) {
            if (userOptional.get().getUserId() == post.getUserId()) {
                Optional<Post> postsOptional = postsRepo.findByUserIdAndTitleAndContent(userIdRequest, post.getTitle(), post.getContent());
                if (postsOptional.isPresent()) {
                    try {
                        postsRepo.delete(post);
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

    @Override
    public ResponseEntity<Page<Post>> findAllLimit10Descending(Pageable pageable) throws MyValidateException {
        try {
            return ResponseEntity.ok(postsRepo.findAll(pageable));
        } catch (Exception e) {
            throw new MyValidateException("error query");
        }
    }

    @Override
    public ResponseEntity<Object> updatePost(HttpServletRequest request, Post post, BindingResult result) throws MyValidateException {
        Map<String, String> mapError = commons.handlesBindingResult(result);
        if (!mapError.isEmpty()) {
            return ResponseEntity.badRequest().body(mapError);
        }
        long userIdRequest = extractDataFromJwt.getUserId(request);
        if (userIdRequest != post.getUserId()) {
            throw new MyValidateException("authentication failed");
        }
        Optional<Post> postsOptional = postsRepo.findByUserIdAndPostId(userIdRequest, post.getUserId());
        if (postsOptional.isPresent()) {
            Post postExist = postsOptional.get();
            postExist.setTitle(post.getTitle());
            postExist.setContent(post.getContent());
            postsRepo.save(postExist);
            return ResponseEntity.ok("posts update success");
        }
        throw new MyValidateException("Can't find this pots to update");
    }
}
