package com.example.personal_blog.service_imp;

import com.example.personal_blog.entity.Account;
import com.example.personal_blog.entity.RoleUser;
import com.example.personal_blog.entity.User;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.jwt.ExtractDataFromJwt;
import com.example.personal_blog.repository.AccountRepo;
import com.example.personal_blog.repository.RoleUserRepo;
import com.example.personal_blog.repository.UserRepo;
import com.example.personal_blog.request.RequestUpdateAccount;
import com.example.personal_blog.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImp implements AccountService {
    private final AccountRepo accountRepo;
    private final UserRepo userRepo;
    private final Commons commons;
    private final RoleUserRepo roleUserRepo;
    private final ExtractDataFromJwt extractDataFromJwt;

    @Override
    @Transactional
    public ResponseEntity<Object> createAccount(Account account, BindingResult bindingResult) throws MyValidateException {
        Map<String, String> mapError = commons.handlesBindingResult(bindingResult);
        Optional<Account> newAccount = accountRepo.findByUserName(account.getUserName());
        if (newAccount.isPresent()) {
            mapError.put("userName", "account already exist");
        }
        if (account.getPassword().length() < 6 || account.getPassword().length() > 50) {
            mapError.put("password", "password must from 6 to 50 keyword");
        }
        if (mapError.isEmpty()) {
            try {
                User user = userRepo.save(new User(0, null, "user", 20, "user@gmail.com","0123456789", "user address"));
                BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
                String passwordEncode = bc.encode(account.getPassword());
                account.setPassword(passwordEncode);
                account.setUserID(user.getUserID());
                accountRepo.save(account);
                roleUserRepo.save(new RoleUser(user.getUserID(), 2));
                return ResponseEntity.ok("Create Account success");
            } catch (Exception ex) {
                throw new MyValidateException(ex.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body(mapError);
        }

    }

    @Override
    public ResponseEntity<Object> updatePassword(RequestUpdateAccount requestUpdateAccount, BindingResult bindingResult) throws MyValidateException {
        Map<String, String> mapError = commons.handlesBindingResult(bindingResult);
        if (!mapError.isEmpty()) {
            return ResponseEntity.badRequest().body(mapError);
        }

        Optional<Account> accountOptional = accountRepo.findByUserName(requestUpdateAccount.getUserName());
        if (!accountOptional.isPresent()) {
            throw new MyValidateException("account does not exist");
        }
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        if (!bc.matches(requestUpdateAccount.getOldPassword(), accountOptional.get().getPassword())) {
            throw new MyValidateException("Old password does not match the stored password");
        }
        Optional<User> userOptional = userRepo.findById(accountOptional.get().getUserID());
        if (userOptional.isPresent()) {
            if (userOptional.get().getUserID() == accountOptional.get().getUserID()) {
                Account account = new Account();
                account.setAccountID(accountOptional.get().getAccountID());
                account.setUserName(requestUpdateAccount.getUserName());
                account.setPassword(bc.encode(requestUpdateAccount.getNewPassword()));
                account.setUserID(accountOptional.get().getUserID());
                try {
                    accountRepo.save(account);
                    return ResponseEntity.ok("account update successful");
                } catch (Exception ex) {
                    throw new MyValidateException("account update failed");
                }
            }
        }
        throw new MyValidateException("account update failed");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteAccount(HttpServletRequest request, String password) throws MyValidateException {
        Optional<Account> accountOptional = accountRepo.findByUserName(extractDataFromJwt.extractUserName(request));
        if (accountOptional.isPresent()) {
            BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
            if (bc.matches(password, accountOptional.get().getPassword())) {
                Optional<User> userOptional = userRepo.findById(accountOptional.get().getUserID());
                if (userOptional.isPresent()) {
                    try {
                        roleUserRepo.deleteRoleUserByUserID(userOptional.get().getUserID());
                        userRepo.delete(userOptional.get());
                        accountRepo.delete(accountOptional.get());
                        return ResponseEntity.ok("account delete success");
                    } catch (Exception ex) {
                        throw new MyValidateException("error query");
                    }
                }
            }
        }
        return ResponseEntity.badRequest().body("account delete failed");
    }
}
