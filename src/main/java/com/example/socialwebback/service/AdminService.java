package com.example.socialwebback.service;

import com.example.socialwebback.dto.AdminUserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    List<AdminUserDto> getUsers(Long roleId);

    void setRole(Long userId, Long roleId);

    void banUser(Long userId);
}
