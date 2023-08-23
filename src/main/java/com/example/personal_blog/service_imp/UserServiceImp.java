package com.example.personal_blog.service_imp;

import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.jwt.ExtractDataFromJwt;
import com.example.personal_blog.repository.UserRepo;
import com.example.personal_blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {
    private final ExtractDataFromJwt extractDataFromJwt;
    private final Commons commons;
    private final UserRepo userRepo;

    @Override
    public ResponseEntity<Object> getAllUser(HttpServletRequest request) throws MyValidateException {
        List<String> listRoleName = extractDataFromJwt.extractListRole(request);
        commons.validateListRoleNameToRoleName("ADMIN", listRoleName);
        return ResponseEntity.ok(userRepo.findAll());
    }
}
