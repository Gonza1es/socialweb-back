package com.example.socialwebback.controller;

import com.example.socialwebback.dto.AdminUserDto;
import com.example.socialwebback.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<AdminUserDto> getUsers() {
        return adminService.getUsers(1L);
    }

    @GetMapping("/moderators")
    public List<AdminUserDto> getModerators() {
        return adminService.getUsers(3L);
    }

    @GetMapping("/setRole/{userId}/{roleId}")
    public void setRole(@PathVariable Long userId,
                        @PathVariable Long roleId) {
        adminService.setRole(userId, roleId);
    }

    @DeleteMapping("/ban/{userId}")
    public void banUser(@PathVariable Long userId) {
        adminService.banUser(userId);
    }
}
