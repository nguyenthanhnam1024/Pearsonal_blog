package com.example.personal_blog.service_imp;

import com.example.personal_blog.entity.Comment;
import com.example.personal_blog.entity.Posts;
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
        long userID = extractDataFromJwt.getUserID(request);
        Optional<User> userOptional = userRepo.findById(userID);
        if (userOptional.isPresent()) {
            Optional<Posts> postsOptional = postsRepo.findByUserIDAndPostsID(userID, comment.getPostsID());
            if (!postsOptional.isPresent()) {
                throw new MyValidateException("posts not found");
            }
            comment.setUserID(userID);
            Object maxCommentID = commentRepo.findMaxCommentIDByUserIDAndPostsID(userID, comment.getPostsID());
            if (maxCommentID != null) {
                comment.setCommentID((long) maxCommentID + 1);
            } else {
                comment.setCommentID(1);
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
    public ResponseEntity<Object> getCommentsByPostsIDAndUserID(long postsID, long userID) throws MyValidateException {
        try {
            return ResponseEntity.ok(commentRepo.findByPostsIDAndUserID(postsID, userID));
        } catch (Exception e) {
            throw new MyValidateException("error query");
        }
    }

    @Override
    public ResponseEntity<Object> deleteComment(HttpServletRequest request, Comment comment) throws MyValidateException {
        long userID = extractDataFromJwt.getUserID(request);
        if (userID != comment.getUserID()) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Comment> commentOptional = commentRepo.findByCommentIDAndPostsIDAndUserID(comment.getCommentID(), comment.getPostsID(), comment.getUserID());
        if (!commentOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        if (!comment.getContent().equals(commentOptional.get().getContent())) {
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
