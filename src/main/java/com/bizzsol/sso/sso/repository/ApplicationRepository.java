package com.bizzsol.sso.sso.repository;

import com.bizzsol.sso.sso.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
