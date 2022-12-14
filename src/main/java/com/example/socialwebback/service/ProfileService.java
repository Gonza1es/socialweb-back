package com.example.socialwebback.service;

import com.example.socialwebback.dto.AddProfileInfo;
import com.example.socialwebback.dto.ProfileDto;
import com.example.socialwebback.dto.SubscriptionsDto;
import com.example.socialwebback.dto.UserProfileDto;
import com.example.socialwebback.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProfileService {

    void editProfile(AddProfileInfo addProfileInfo, MultipartFile avatar, MultipartFile cover) throws IOException;

    void createNewProfile(User user);

    ProfileDto getProfileInfoCurrentUser();

    UserProfileDto getProfileInfo(Long userId);

    void subscribe(String username);

    void unsubscribe(String username);

    List<SubscriptionsDto> getSubscriptions();
}
