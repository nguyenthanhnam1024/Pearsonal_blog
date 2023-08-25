package com.example.personal_blog.service_imp;

import com.example.personal_blog.entity.Account;
import com.example.personal_blog.entity.Role;
import com.example.personal_blog.entity.RoleUser;
import com.example.personal_blog.entity.User;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.jwt.ExtractDataFromJwt;
import com.example.personal_blog.repository.AccountRepo;
import com.example.personal_blog.repository.RoleRepo;
import com.example.personal_blog.repository.RoleUserRepo;
import com.example.personal_blog.repository.UserRepo;
import com.example.personal_blog.response.ResponseLogin;
import com.example.personal_blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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
    private final RoleUserRepo roleUserRepo;
    private final RoleRepo roleRepo;

    @Override
    public ResponseEntity<Object> getAllUser(HttpServletRequest request) throws MyValidateException {
        List<String> listRoleName = extractDataFromJwt.extractListRole(request);
        commons.validateListRoleNameToRoleName("ADMIN", listRoleName);
        return ResponseEntity.ok(userRepo.findAll());
    }

    @Override
    public ResponseEntity<Object> updateInfoUser(HttpServletRequest request, User user, BindingResult result) throws MyValidateException {
        Map<String, String> mapError = commons.handlesBindingResult(result);
        Optional<Account> accountOptional = accountRepo.findByUserName(extractDataFromJwt.extractUserName(request));
        if (accountOptional.isPresent()) {
            Optional<User> oldUserOptional = userRepo.findById(accountOptional.get().getUserID());
            if (oldUserOptional.isPresent()) {
                if (!oldUserOptional.get().getEmail().equals(user.getEmail())) {
                    Optional<User> userFindByEmail = userRepo.findByEmail(user.getEmail());
                    if (userFindByEmail.isPresent()) {
                        mapError.put("email", "email " + user.getEmail() + " is already in use");
                    }
                }
                if (!oldUserOptional.get().getPhoneNumber().equals(user.getPhoneNumber())) {
                    Optional<User> userFindByPhone = userRepo.findUserByPhoneNumber(user.getPhoneNumber());
                    if (userFindByPhone.isPresent()) {
                        mapError.put("phoneNumber", "phone number " + user.getPhoneNumber() + " is already in use");
                    }
                }
                if (!mapError.isEmpty()) {
                    return ResponseEntity.badRequest().body(mapError);
                }
                user.setUserID(oldUserOptional.get().getUserID());
                try {
                    userRepo.save(user);
                    return ResponseEntity.ok("user update success");
                } catch (Exception ex) {
                    throw new MyValidateException("error query");
                }
            }
        }
        throw new MyValidateException("authentication error");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> updateRoleForUserByAdmin(HttpServletRequest request, ResponseLogin responseLogin) throws MyValidateException {
        commons.validateListRoleNameToRoleName("ADMIN", extractDataFromJwt.extractListRole(request));
        Optional<User> userOptional = userRepo.findByEmail(responseLogin.getEmail());
        if (userOptional.isPresent()) {
            Optional<Role> roleOptional = roleRepo.findByRoleName(responseLogin.getRoleName());
            if (roleOptional.isPresent()) {
                Optional<RoleUser> roleUserOptional = roleUserRepo.findByUserID(userOptional.get().getUserID());
                if (roleUserOptional.isPresent()) {
                    if (roleUserOptional.get().getRoleID() != roleOptional.get().getRoleID()) {
                        roleUserRepo.delete(roleUserOptional.get());
                        roleUserRepo.save(new RoleUser(userOptional.get().getUserID(), roleOptional.get().getRoleID()));
                    }
                    return ResponseEntity.ok("user update success");
                }
            }
        }
        throw new MyValidateException("info for update user not found");
    }
}
