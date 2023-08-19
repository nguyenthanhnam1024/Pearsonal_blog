package com.example.personal_blog.ServiceImp;

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
                .orElseThrow(() -> new UsernameNotFoundException("Account not found : " + userName));

        User user = userRepo.findByAccountID(account.getAccountID())
                .orElseThrow(() -> new UsernameNotFoundException("User not found for account: " + account.getUserName()));

        RoleUser roleUser = roleUserRepo.findByUserID(user.getUserID())
                .orElseThrow(() -> new UsernameNotFoundException("Role not found for user: " + user.getName()));

        Role role = roleRepo.findById(roleUser.getRoleID())
                .orElseThrow(() -> new UsernameNotFoundException("Role not found for user: " + user.getName()));

        if (role.getRoleName().equals("ADMIN") && user.getUserID() != 0) {
            throw new UsernameNotFoundException("authenticate error");
        }
        return userDetailsImp.build(account.getUserName(), account.getPassword(), role.getRoleName());
    }
}
