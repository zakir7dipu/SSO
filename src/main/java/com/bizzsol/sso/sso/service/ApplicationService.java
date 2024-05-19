package com.bizzsol.sso.sso.service;

import com.bizzsol.sso.sso.model.Application;
import com.bizzsol.sso.sso.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;
    public Application save(Application application) {
        LocalDateTime nowTime = LocalDateTime.now();
        String accessKey = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        String secreteAccessKey = UUID.randomUUID().toString().replace("-", "").substring(0, 32);

        application.setCreated_at(nowTime);
        application.setUpdated_at(nowTime);
        application.setAccess_key(accessKey);
        application.setSecrete_access_key(secreteAccessKey);
        return applicationRepository.save(application);
    }

    public List<Application> all() {
        return applicationRepository.findAll();
    }

    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }

    public Application update(Long id, Application updatedApplication) {
        LocalDateTime nowTime = LocalDateTime.now();

        Application oldApplication = this.findById(id).orElse(null);
        if(oldApplication != null) {
            oldApplication.setApp_name(updatedApplication.getApp_name() != null && !updatedApplication.getApp_name().equals("") ? updatedApplication.getApp_name():oldApplication.getApp_name());

            oldApplication.setDomain_authentication_url(updatedApplication.getDomain_authentication_url() != null && !updatedApplication.getDomain_authentication_url().equals("") ? updatedApplication.getDomain_authentication_url() : oldApplication.getDomain_authentication_url());

            oldApplication.setRedirect_url(updatedApplication.getRedirect_url() != null && !updatedApplication.getRedirect_url().equals("") ? updatedApplication.getRedirect_url() : oldApplication.getRedirect_url());

            oldApplication.setUpdated_at(nowTime);
        }
        return applicationRepository.save(oldApplication);
    }

    public void deleteByID(Long id) {
        applicationRepository.deleteById(id);
    }
}
