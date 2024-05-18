package com.bizzsol.sso.sso.dao;

import com.bizzsol.sso.sso.model.User;

public interface UserDao {
    User findByUserName(String userName);
}
