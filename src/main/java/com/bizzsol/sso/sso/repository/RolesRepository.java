package com.bizzsol.sso.sso.repository;

import com.bizzsol.sso.sso.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Role, Long> {
}
