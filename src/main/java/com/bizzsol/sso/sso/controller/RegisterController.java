package com.bizzsol.sso.sso.controller;

import com.bizzsol.sso.sso.model.User;
import com.bizzsol.sso.sso.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("user", new User()); // Create an empty User object for form binding
        return "auth/register"; // Thymeleaf template name
    }

    @PostMapping
    public String createUser(@ModelAttribute User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        try {
            // Success scenario (e.g., redirect to login page)
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful!");
            return "redirect:/login"; // Or your login page path
        } catch (Exception exception) {
            return "auth/register"; // Retain errors in the form
        }
        // Implement user registration logic (validation, saving to database, etc.)
//        if (result.hasErrors()) {
//
//        }


    }
}
