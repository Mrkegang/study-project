package com.kgstudy.service;

import com.kgstudy.entity.Account;
import com.kgstudy.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author kg
 * @version 1.0
 * @description
 * @date 2023/4/13 23:15
 */
@Service
public class AuthorizeService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String text) throws UsernameNotFoundException {
        if (text == null) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        Account account = userMapper.findAccountByNameOrEmail(text);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }
}
