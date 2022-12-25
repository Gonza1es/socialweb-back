package com.example.socialwebback.service.Impl;

import com.example.socialwebback.dto.AdminUserDto;
import com.example.socialwebback.model.Profile;
import com.example.socialwebback.model.Role;
import com.example.socialwebback.model.User;
import com.example.socialwebback.repository.ProfileRepository;
import com.example.socialwebback.repository.RoleRepository;
import com.example.socialwebback.repository.UserRepository;
import com.example.socialwebback.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;

    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(RoleRepository roleRepository,
                            ProfileRepository profileRepository,
                            UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AdminUserDto> getUsers(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role != null) {
            if (roleId == 1L) {
                return role.getUsers()
                        .stream()
                        .filter(user -> user.getRoles().size() == 1)
                        .map(this::convertToAdminUserDto)
                        .collect(Collectors.toList());
            } else
                return role.getUsers()
                    .stream()
                    .map(this::convertToAdminUserDto)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void setRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Role role = roleRepository.findByName("ROLE_MODERATOR");
            if (roleId == 1L) {
                user.getRoles().add(role);
                userRepository.saveAndFlush(user);
            } else if (roleId == 3L) {
                user.getRoles().remove(role);
                userRepository.saveAndFlush(user);
            }
        }
    }

    @Override
    @Transactional
    public void banUser(Long userId) {
        profileRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    private AdminUserDto convertToAdminUserDto(User user) {
        AdminUserDto dto = new AdminUserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        Profile profile = profileRepository.findByUserId(user.getId());
        dto.setAvatarId(profile.getAvatar().getId());
        dto.setProfileAlias(profile.getAlias());

        return dto;
    }
}
