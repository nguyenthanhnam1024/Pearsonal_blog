package com.example.personal_blog.service_imp;

import com.example.personal_blog.config.UserDetailsImp;
import com.example.personal_blog.entity.Account;
import com.example.personal_blog.entity.Role;
import com.example.personal_blog.entity.RoleUser;
import com.example.personal_blog.entity.User;
import com.example.personal_blog.repository.AccountRepo;
import com.example.personal_blog.repository.RoleRepo;
import com.example.personal_blog.repository.RoleUserRepo;
import com.example.personal_blog.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {
    private final AccountRepo accountRepo;
    private final UserRepo userRepo;
    private final RoleUserRepo roleUserRepo;
    private final RoleRepo roleRepo;
    private final UserDetailsImp userDetailsImp;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Account account = accountRepo.findByUserName(userName)
                .orElseThrow(() -> new AuthenticationException("Account not found : " + userName) {
                });

        User user = userRepo.findById(account.getUserID())
                .orElseThrow(() -> new UsernameNotFoundException("User not found for account: " + account.getUserName()));

        RoleUser roleUser = roleUserRepo.findByUserId(user.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Role not found for user: " + user.getName()));

        Role role = roleRepo.findById(roleUser.getRoleId())
                .orElseThrow(() -> new UsernameNotFoundException("Role not found for user: " + user.getName()));

        if (role.getRoleName().equals("ADMIN") && user.getUserId() != 1) {
            throw new UsernameNotFoundException("authenticate error");
        }
        return userDetailsImp.build(account.getUserName(), account.getPassword(), user, role.getRoleName());
    }
}
