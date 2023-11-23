package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RolesRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminsController {
    private final UserService userService;
    private final RolesRepository rolesRepository;

    @Autowired
    public AdminsController(UserService userService, RolesRepository rolesRepository) {
        this.userService = userService;
        this.rolesRepository = rolesRepository;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/allUsers";
    }

    @GetMapping("/registration")
    public String addUser(@ModelAttribute("user") User user, Model model) {
        List<Role> roles = (List<Role>) rolesRepository.findAll();
        model.addAttribute("allRoles", roles);
        return "/auth/registration";
    }

    @GetMapping("/{id}")
    public String showUserInfo(@PathVariable("id") Long id, Model model) {
        User user = userService.readUser(id);
        model.addAttribute("user", user);
        model.addAttribute("userRoles", user.getRoles());
        return "/user/userInfo";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        User user = userService.readUser(id);
        model.addAttribute("user", user);
        List<Role> roles = (List<Role>) rolesRepository.findAll();
        model.addAttribute("allRoles", roles);

        return "/admin/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, @PathVariable("id") Long id,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/edit";
        }
        userService.updateUser(user, id);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
