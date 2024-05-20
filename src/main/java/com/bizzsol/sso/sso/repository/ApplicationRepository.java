package com.bizzsol.sso.sso.repository;

import com.bizzsol.sso.sso.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByAppName(String appName);
}
