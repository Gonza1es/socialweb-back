package com.example.socialwebback.service;

import com.example.socialwebback.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void register(User user);

    User getUserByUsername(String Username);

    void registAdmin(User user);
}
