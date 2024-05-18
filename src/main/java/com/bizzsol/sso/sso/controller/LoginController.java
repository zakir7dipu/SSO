package com.bizzsol.sso.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
//@RestController
@RequestMapping("/login")
public class LoginController {

//    @GetMapping
//    public String loginPage() {
//        return "auth/fancy-login"; // View name for login page
//    }

    @GetMapping
    public String loginPage() {
        return "auth/auth_login"; // View name for login page
    }
}
