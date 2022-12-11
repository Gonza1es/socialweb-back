package com.example.socialwebback.controller;

import com.example.socialwebback.dto.AddProfileInfo;
import com.example.socialwebback.dto.ProfileDto;
import com.example.socialwebback.dto.UserProfileDto;
import com.example.socialwebback.model.User;
import com.example.socialwebback.service.ProfileService;
import com.example.socialwebback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/profile")
public class UserProfileController {

    private ProfileService profileService;

    private UserService userService;

    @Autowired
    public UserProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping("/current")
    public ProfileDto getCurrentProfile() {
        return profileService.getProfileInfoCurrentUser();
    }

    @GetMapping("/{username}")
    public UserProfileDto getProfileByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return profileService.getProfileInfo(user.getId());
    }

    @GetMapping("/subscribe/{username}")
    public void subscribe(@PathVariable String username) {
        profileService.subscribe(username);
    }

    @GetMapping("/unsubscribe/{username}")
    public void unsubscribe(@PathVariable String username) {
        profileService.unsubscribe(username);
    }


    @PostMapping(value = "/editProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void editProfile(@RequestPart(required = false) String alias,
                            @RequestPart(required = false) String status,
                            @RequestPart(required = false) MultipartFile avatar,
                            @RequestPart(required = false) MultipartFile cover) throws IOException {
        AddProfileInfo addProfileInfo = new AddProfileInfo(alias, status);
        profileService.editProfile(addProfileInfo, avatar, cover);
    }
}
