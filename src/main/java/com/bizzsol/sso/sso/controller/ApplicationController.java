package com.bizzsol.sso.sso.controller;

import com.bizzsol.sso.sso.model.Application;
import com.bizzsol.sso.sso.security.ApplicationNotFoundException;
import com.bizzsol.sso.sso.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

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
    public String createApp(@Valid @ModelAttribute Application application, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        try {
            applicationService.save(application);
            redirectAttributes.addFlashAttribute("successMessage", "App created successful!");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMassage", exception.getMessage());
        }
        return "redirect:/application";
    }

    @GetMapping("/show")
    public ResponseEntity<?> show(@RequestParam("name") String app_name) {
        try {
            Optional<Application> application = applicationService.findByAppName(app_name);
            return application.map(ResponseEntity::ok)
                    .orElseThrow(() -> new ApplicationNotFoundException(app_name));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute @RequestParam Long id, Application application, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        try {
            applicationService.update(id, application);
            redirectAttributes.addFlashAttribute("successMessage", "App updated successful!");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMassage", exception.getMessage());
        }
        return "redirect:/application";
    }

    @GetMapping("/delete")
    public String destroy(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            applicationService.deleteByID(id);
            redirectAttributes.addFlashAttribute("successMessage", "App destroyed successful!");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMassage", exception.getMessage());
        }
        return "redirect:/application";
    }
}
