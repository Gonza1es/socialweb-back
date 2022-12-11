package com.example.socialwebback.service.Impl;

import com.example.socialwebback.model.Role;
import com.example.socialwebback.model.User;
import com.example.socialwebback.repository.RoleRepository;
import com.example.socialwebback.repository.UserRepository;
import com.example.socialwebback.service.ProfileService;
import com.example.socialwebback.service.UserService;
import com.example.socialwebback.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final ProfileService profileService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, ProfileService profileService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileService = profileService;
    }

    @Override
    public void register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setIsFirstVisit(Boolean.TRUE);


        userRepository.saveAndFlush(user);

        profileService.createNewProfile(getUserByUsername(user.getUsername()));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void setFirstVisit() {
        User user = userRepository.findByUsername(UserUtils.getCurrentUser().getUsername());
        if (user.getIsFirstVisit().equals(Boolean.TRUE)) {
            user.setIsFirstVisit(Boolean.FALSE);
            userRepository.save(user);
        }
    }


}
