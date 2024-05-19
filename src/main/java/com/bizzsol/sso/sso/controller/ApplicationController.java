package com.bizzsol.sso.sso.controller;

import com.bizzsol.sso.sso.model.Application;
import com.bizzsol.sso.sso.model.User;
import com.bizzsol.sso.sso.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    ApplicationService applicationService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("app", new Application());
        model.addAttribute("apps", applicationService.all());
        return "admin/application";
    }


    @PostMapping
    public String createUser(@Valid @ModelAttribute Application application, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        try {
            applicationService.save(application);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful!");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMassage", exception.getMessage());
        }
        return "redirect:/application";
    }

    @GetMapping("/show")
    public ResponseEntity<?> show(@RequestBody Application application) {
        return ResponseEntity.ok("ok");
    }

//    @PutMapping
//    public String update() {
//        return "ok";
//    }
}
