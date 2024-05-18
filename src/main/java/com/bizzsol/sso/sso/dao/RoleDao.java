package com.bizzsol.sso.sso.dao;

import com.bizzsol.sso.sso.model.Role;

public interface RoleDao {
    public Role findRoleByName(String theRoleName);
}
