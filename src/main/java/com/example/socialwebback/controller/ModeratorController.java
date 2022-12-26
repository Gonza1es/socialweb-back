package com.example.socialwebback.controller;

import com.example.socialwebback.dto.AdminUserDto;
import com.example.socialwebback.dto.ReportDto;
import com.example.socialwebback.service.AdminService;
import com.example.socialwebback.service.ModerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moder")
@CrossOrigin
public class ModeratorController {

    private final ModerService moderService;
    private final AdminService adminService;

    public ModeratorController(ModerService moderService,
                               AdminService adminService) {
        this.moderService = moderService;
        this.adminService = adminService;
    }

    @GetMapping("/reportList")
    public List<ReportDto> getReportList() {
        return moderService.getReports();
    }

    @DeleteMapping("/reject/{reportId}")
    public void rejectReport(@PathVariable Long reportId) {
        moderService.deleteReport(reportId);
    }

    @DeleteMapping("/deletePost/{reportId}")
    public void deletePost(@PathVariable Long reportId) {
        moderService.deletePost(reportId);
    }

    @DeleteMapping("/deleteUser/{reportId}")
    public void deleteUserByReport(@PathVariable Long reportId) {
        moderService.deleteUser(reportId);
    }

    @DeleteMapping("/ban/{userId}")
    public void banUser(@PathVariable Long userId){
        moderService.deleteUserBy(userId);
    }

    @GetMapping("/users")
    public List<AdminUserDto> getUsers(){
        return adminService.getUsers(1L);
    }
}
