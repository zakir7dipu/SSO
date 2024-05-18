package com.bizzsol.sso.sso.repository;


import com.bizzsol.sso.sso.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
