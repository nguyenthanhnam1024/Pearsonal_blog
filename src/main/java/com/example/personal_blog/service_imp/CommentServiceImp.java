package com.example.personal_blog.service_imp;

import com.example.personal_blog.entity.Comment;
import com.example.personal_blog.entity.Post;
import com.example.personal_blog.entity.User;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.jwt.ExtractDataFromJwt;
import com.example.personal_blog.repository.CommentRepo;
import com.example.personal_blog.repository.PostsRepo;
import com.example.personal_blog.repository.UserRepo;
import com.example.personal_blog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImp implements CommentService {
    private final CommentRepo commentRepo;
    private final ExtractDataFromJwt extractDataFromJwt;
    private final Commons commons;
    private final UserRepo userRepo;
    private final PostsRepo postsRepo;

    @Override
    public ResponseEntity<Object> createComment(HttpServletRequest request, Comment comment, BindingResult result) throws MyValidateException {
        Map<String, String> errorMap = commons.handlesBindingResult(result);
        if (!errorMap.isEmpty()) {
            return ResponseEntity.badRequest().body(errorMap);
        }
        long userIdAddComment = extractDataFromJwt.getUserId(request);
        Optional<User> userOptional = userRepo.findById(userIdAddComment);
        if (userOptional.isPresent()) {
            Optional<Post> postsOptional = postsRepo.findByUserIdAndPostId(comment.getUserIdOwnComment(), comment.getPostId());
            if (!postsOptional.isPresent()) {
                throw new MyValidateException("posts not found");
            }
            comment.setUserIdAddComment(userIdAddComment);
            Object maxCommentID = commentRepo.findMaxCommentIdByUserIdOwnAndPostId(comment.getUserIdOwnComment(), comment.getPostId());
            if (maxCommentID != null) {
                comment.setCommentId((long) maxCommentID + 1);
            } else {
                comment.setCommentId(1);
            }
            try {
                commentRepo.save(comment);
                return ResponseEntity.ok("comment create success");
            } catch (Exception e) {
                throw new MyValidateException("error query");
            }
        }
        throw new MyValidateException("authentication error");
    }

    @Override
    public ResponseEntity<Object> getCommentsByPostIdAndUserIdOwn(long postId, long userIdOwn) throws MyValidateException {
        try {
            List<Comment> listComment = commentRepo.findByPostIdAndUserIdOwnComment(postId, userIdOwn);
            Collections.reverse(listComment);
            return ResponseEntity.ok(listComment);
        } catch (Exception e) {
            throw new MyValidateException("error query");
        }
    }

    @Override
    public ResponseEntity<Object> deleteComment(HttpServletRequest request, Comment comment) throws MyValidateException {
        long userID = extractDataFromJwt.getUserId(request);
        if (userID != comment.getUserIdOwnComment()) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Comment> commentOptional = commentRepo.findByCommentIdAndPostIdAndUserIdOwnComment(comment.getCommentId(), comment.getPostId(), comment.getUserIdOwnComment());
        if (!commentOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            commentRepo.delete(comment);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new MyValidateException("error query");
        }
    }
}
