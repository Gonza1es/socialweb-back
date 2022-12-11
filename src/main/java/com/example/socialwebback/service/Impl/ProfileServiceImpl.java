package com.example.socialwebback.service.Impl;

import com.example.socialwebback.dto.AddProfileInfo;
import com.example.socialwebback.dto.ProfileDto;
import com.example.socialwebback.dto.UserProfileDto;
import com.example.socialwebback.model.Avatar;
import com.example.socialwebback.model.Cover;
import com.example.socialwebback.model.Profile;
import com.example.socialwebback.model.User;
import com.example.socialwebback.repository.AvatarRepository;
import com.example.socialwebback.repository.CoverRepository;
import com.example.socialwebback.repository.ProfileRepository;
import com.example.socialwebback.repository.UserRepository;
import com.example.socialwebback.service.ProfileService;
import com.example.socialwebback.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProfileServiceImpl implements ProfileService {

    private ProfileRepository profileRepository;

    private AvatarRepository avatarRepository;

    private CoverRepository coverRepository;

    private UserRepository userRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository,
                              AvatarRepository avatarRepository,
                              CoverRepository coverRepository,
                              UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.avatarRepository = avatarRepository;
        this.coverRepository = coverRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProfileDto getProfileInfoCurrentUser() {
        Profile currentProfile = profileRepository.findByUserId(UserUtils.getCurrentUser().getId());
        return convertToProfileDto(currentProfile);
    }



    @Override
    public UserProfileDto getProfileInfo(Long userId) {
        Profile profile = profileRepository.findByUserId(userId);
        return convertToUserProfileDto(profile);
    }

    @Override
    public void subscribe(String username) {
        User user = userRepository.findByUsername(username);
        Profile userProfile = profileRepository.findByUserId(user.getId());
        Profile currentUserProfile = profileRepository.findByUserId(UserUtils.getCurrentUser().getId());
        userProfile.getSubscribers().add(currentUserProfile);

        profileRepository.save(userProfile);
    }

    @Override
    public void unsubscribe(String username) {
        User user = userRepository.findByUsername(username);
        Profile userProfile = profileRepository.findByUserId(user.getId());
        Profile currentUserProfile = profileRepository.findByUserId(UserUtils.getCurrentUser().getId());
        userProfile.getSubscribers().remove(currentUserProfile);

        profileRepository.save(userProfile);
    }

    private UserProfileDto convertToUserProfileDto(Profile profile) {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setAliasProfile(profile.getAlias());
        userProfileDto.setStatus(profile.getStatus());
        if (profile.getAvatar() != null)
            userProfileDto.setAvatarId(profile.getAvatar().getId());
        if (profile.getCover() != null)
            userProfileDto.setCoverId(profile.getCover().getId());
        userProfileDto.setSubscribersCount(profile.getSubscribers().size());
        userProfileDto.setIsSubscribed(profile.getSubscribers().contains(profileRepository.findByUserId(UserUtils.getCurrentUser().getId())));

        return userProfileDto;
    }

    @Transactional
    @Override
    public void editProfile(AddProfileInfo addProfileInfo, MultipartFile addAvatar, MultipartFile addCover) throws IOException {
        Profile profile = profileRepository.findByUserId(UserUtils.getCurrentUser().getId());
        if (addProfileInfo.getStatus() != null)
            profile.setStatus(addProfileInfo.getStatus());
        if (addProfileInfo.getAlias() != null)
            profile.setAlias(addProfileInfo.getAlias());
        if (addAvatar != null) {
            Avatar avatar = convertToAvatar(addAvatar);
            profile.addAvatar(avatar);
        }
        if (addCover != null) {
            Cover cover = convertToCover(addCover);
            profile.addCover(cover);
        }
        profileRepository.save(profile);
    }

    @Override
    public void createNewProfile(User user) {
        Profile newProfile = new Profile();
        newProfile.setUserId(user.getId());

        profileRepository.save(newProfile);
    }

    private Avatar convertToAvatar(MultipartFile multipartFile) throws IOException {
        Avatar avatar = new Avatar();
        avatar.setName(multipartFile.getName());
        avatar.setOriginalFileName(multipartFile.getOriginalFilename());
        avatar.setSize(multipartFile.getSize());
        avatar.setContentType(multipartFile.getContentType());
        avatar.setImageBytes(multipartFile.getBytes());

        return avatar;
    }

    private Cover convertToCover(MultipartFile multipartFile) throws IOException {
        Cover cover = new Cover();
        cover.setName(multipartFile.getName());
        cover.setOriginalFileName(multipartFile.getOriginalFilename());
        cover.setSize(multipartFile.getSize());
        cover.setContentType(multipartFile.getContentType());
        cover.setImageBytes(multipartFile.getBytes());

        return cover;
    }

    private ProfileDto convertToProfileDto(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setAliasProfile(profile.getAlias());
        profileDto.setStatus(profile.getStatus());
        if (profile.getAvatar() != null)
            profileDto.setAvatarId(profile.getAvatar().getId());
        if (profile.getCover() != null)
            profileDto.setCoverId(profile.getCover().getId());
        profileDto.setSubscribersCount(profile.getSubscribers().size());
        return profileDto;
    }
}
