package com.bizzsol.sso.sso.repository;


import com.bizzsol.sso.sso.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.userName <> ?1 AND 'ROLE_ADMIN' NOT IN (SELECT r.name FROM u.roles r)")
    List<User> findUsersExcludingCurrentAndAdmins(String currentUsername);
    Optional<User> findUserByUserName(String userName);
}
