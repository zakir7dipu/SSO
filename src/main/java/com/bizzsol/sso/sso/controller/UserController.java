package com.bizzsol.sso.sso.controller;

import com.bizzsol.sso.sso.service.ApplicationService;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ApplicationService applicationService;

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
        model.addAttribute("apps", applicationService.all());
        return "admin/users";
    }

    @PostMapping("/user")
    public String create(@Valid @ModelAttribute User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        try {
            String pwd = "123@456";
            user.setPassword(passwordEncoder.encode(pwd));
            User newUser = userService.createNewUserSave(user);
            newUser.setApplications(user.getApplications());
            redirectAttributes.addFlashAttribute("successMessage", "User created successful!\nThe password is " + pwd);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMassage", exception.getMessage());
        }
        return "redirect:/user";
    }


    @GetMapping("/user/show")
    public ResponseEntity<?> show(@RequestParam String username) {
        try {
            User user = userService.findUserByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            System.out.println(user.getApplications());
            return ResponseEntity.ok(user);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/user/update")
    public String update(@Valid @ModelAttribute @RequestParam String username, User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        try {
            List apps = (List) user.getApplications();
            User updatedUser = userService.genUserUpdate(username, user);
            userService.syncUserApplications(updatedUser.getId(), apps);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successful!");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMassage", exception.getMessage());
        }
        return "redirect:/user";
    }

    @GetMapping("/user/delete")
    public String destroy(@RequestParam String username, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUserName(username);
            userService.deleteUserByID(user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "User destroyed successful!");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMassage", exception.getMessage());
        }
        return "redirect:/user";
    }
}
