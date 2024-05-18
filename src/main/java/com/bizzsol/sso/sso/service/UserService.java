package com.bizzsol.sso.sso.service;

import com.bizzsol.sso.sso.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public User findByUserName(String userName);
}
