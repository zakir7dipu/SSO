package com.bizzsol.sso.sso.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bizzsol.sso.sso.model.User;
import com.bizzsol.sso.sso.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String showDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/user")
    public String index(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("users", userService.findUsersExcludingCurrentAndAdmins());
        return "admin/users";
    }

    @PostMapping("/user")
    public String create(@Valid @ModelAttribute User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        try {
            String pwd = "123@456";
            user.setPassword(passwordEncoder.encode(pwd));
            userService.genUserSave(user);
            redirectAttributes.addFlashAttribute("successMessage", "User created successful!\nThe password is "+pwd);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMassage", exception.getMessage());
        }
        return "redirect:/user";
    }

    @GetMapping("/user/show")
    public ResponseEntity<?> show(@RequestParam String username) {
        try {
            Optional<User> user = userService.findUserByUserName(username);
            return user.map(ResponseEntity::ok)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/user/update")
    public String update(@Valid @ModelAttribute @RequestParam String username, User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.genUserUpdate(username, user);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successful!");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMassage", exception.getMessage());
        }
        return "redirect:/user";
    }

}
