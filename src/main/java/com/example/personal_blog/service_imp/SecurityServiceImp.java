package com.example.personal_blog.service_imp;

import com.example.personal_blog.config.UserDetailsImp;
import com.example.personal_blog.entity.Account;
import com.example.personal_blog.entity.Role;
import com.example.personal_blog.entity.RoleUser;
import com.example.personal_blog.entity.User;
import com.example.personal_blog.exception.MyValidateException;
import com.example.personal_blog.jwt.JwtUtils;
import com.example.personal_blog.repository.AccountRepo;
import com.example.personal_blog.repository.RoleRepo;
import com.example.personal_blog.repository.RoleUserRepo;
import com.example.personal_blog.repository.UserRepo;
import com.example.personal_blog.response.ResponseLogin;
import com.example.personal_blog.service.SecurityService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SecurityServiceImp implements SecurityService {
    private final Commons commons;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AccountRepo accountRepo;
    private final UserRepo userRepo;
    private final RoleUserRepo roleUserRepo;
    private final RoleRepo roleRepo;

    @Override
    public void login(HttpServletResponse response, BindingResult bindingResult, Account account) throws MyValidateException {
        Map<String, String> mapError = commons.handlesBindingResult(bindingResult);
        if (!mapError.isEmpty()) {
            ResponseEntity.status(400).body(mapError);
            return;
        }


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        account.getUserName(),
                        account.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwt((UserDetailsImp) authentication.getPrincipal());
        response.addHeader("Authorization", "Bearer "+jwt);

        ResponseLogin responseLogin = new ResponseLogin();
        try {
            Optional<Account> accountExist = accountRepo.findByUserName(account.getUserName());
            Optional<User> user = userRepo.findByAccountID(accountExist.get().getAccountID());
            Optional<RoleUser> roleUser = roleUserRepo.findByUserID(user.get().getUserID());
            Optional<Role> role = roleRepo.findById(roleUser.get().getRoleID());
            ModelMapper modelMapper = new ModelMapper();
            responseLogin = modelMapper.map(user, ResponseLogin.class);
            responseLogin.setRoleName(role.get().getRoleName());
        } catch (Exception ex) {
            throw new MyValidateException("error validation");
        }
        ResponseEntity.ok(responseLogin);
    }
}
