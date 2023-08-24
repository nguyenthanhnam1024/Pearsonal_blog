package com.example.personal_blog.service_imp;

import com.example.personal_blog.entity.Account;
import com.example.personal_blog.entity.User;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.jwt.ExtractDataFromJwt;
import com.example.personal_blog.repository.AccountRepo;
import com.example.personal_blog.repository.UserRepo;
import com.example.personal_blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {
    private final ExtractDataFromJwt extractDataFromJwt;
    private final Commons commons;
    private final UserRepo userRepo;
    private final AccountRepo accountRepo;

    @Override
    public ResponseEntity<Object> getAllUser(HttpServletRequest request) throws MyValidateException {
        List<String> listRoleName = extractDataFromJwt.extractListRole(request);
        commons.validateListRoleNameToRoleName("ADMIN", listRoleName);
        return ResponseEntity.ok(userRepo.findAll());
    }

    @Override
    public ResponseEntity<Object> updateInfoUser(HttpServletRequest request, User user, BindingResult result) throws MyValidateException {
        Map<String, String> mapError = commons.handlesBindingResult(result);
        if (!user.getEmail().equals("user@gmail.com")) {
            Optional<User> userOptional = userRepo.findByEmail(user.getEmail());
            if (userOptional.isPresent()) {
                mapError.put("email", "email '"+user.getEmail()+"' is already in use");
            }
        }
        if (!mapError.isEmpty()) {
            return ResponseEntity.badRequest().body(mapError);
        }
        Optional<Account> accountOptional = accountRepo.findByUserName(extractDataFromJwt.extractUserName(request));
        if (accountOptional.isPresent()) {
            Optional<User> oldUserOptional = userRepo.findById(accountOptional.get().getUserID());
            if (oldUserOptional.isPresent()) {
               if (oldUserOptional.get().getEmail().equals("user@gmail.com")) {
                   user.setUserID(oldUserOptional.get().getUserID());
                   try {
                       userRepo.save(user);
                       return ResponseEntity.ok("user update success");
                   } catch (Exception ex) {
                       throw new MyValidateException("error query");
                   }
               }
               throw new MyValidateException("cant use email : " + user.getEmail());
            }
        }
        throw new MyValidateException("authentication error");
    }
}
