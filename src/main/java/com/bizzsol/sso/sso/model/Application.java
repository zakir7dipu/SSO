package com.bizzsol.sso.sso.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "app_name", nullable = false, unique = true)
    private String app_name;
    @Column(name = "domain_authentication_url", nullable = false, unique = true)
    private String domain_authentication_url;
    @Column(name = "redirect_url", nullable = false, unique = true)
    private String redirect_url;
    @Column(name = "access_key", nullable = false, unique = true)
    private String access_key;
    @Column(name = "secrete_access_key", nullable = false, unique = true)
    private String secrete_access_key;
    @Column(name = "created_at")
    private LocalDateTime created_at;
    @Column(name = "updated_at")
    private LocalDateTime updated_at;
}
