package com.bizzsol.sso.sso.service;

import com.bizzsol.sso.sso.model.Role;
import com.bizzsol.sso.sso.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesService {

    @Autowired
    private RolesRepository rolesRepository;

    public Role save(Role role) {
        return rolesRepository.save(role);
    }
}
