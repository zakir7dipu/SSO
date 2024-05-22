package com.bizzsol.sso.sso.controller;

import com.bizzsol.sso.sso.model.User;
import com.bizzsol.sso.sso.service.UserServiceImpl;
import com.bizzsol.sso.sso.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/sso")
public class SSOFeaturesController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<?> getSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(jwtUtil.generateToken(username));
    }

    @GetMapping("/user/{token}")
    public ResponseEntity<?> getUser(@PathVariable("token") String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(jwtUtil.validateToken(token, username)){
            Optional<User> user = userService.findUserByUserName(username);
            return user.map(ResponseEntity::ok)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/validate/{token}")
    private ResponseEntity<?> validateToken(@PathVariable("token") String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(jwtUtil.validateToken(token, username));
    }
}
