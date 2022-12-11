package com.example.socialwebback.service.Impl;

import com.example.socialwebback.dto.PostDto;
import com.example.socialwebback.model.Cover;
import com.example.socialwebback.model.Image;
import com.example.socialwebback.model.Post;
import com.example.socialwebback.model.Profile;
import com.example.socialwebback.repository.PostRepository;
import com.example.socialwebback.repository.ProfileRepository;
import com.example.socialwebback.service.PostService;
import com.example.socialwebback.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private ProfileRepository profileRepository;

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(ProfileRepository profileRepository, PostRepository postRepository) {
        this.profileRepository = profileRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void createPost(String text, MultipartFile file) throws IOException {
        Profile profile = profileRepository.findByUserId(UserUtils.getCurrentUser().getId());
        Post post = new Post();
        post.setText(text);
        if (file != null && !file.isEmpty()) {
            Image image = convertToImage(file);
            post.addImage(image);
        }
        post.setCreationDate(LocalDateTime.now());
        profile.addPost(post);
        profileRepository.saveAndFlush(profile);
    }

    @Override
    public List<PostDto> getPostCurrentProfile() {
        Profile profile = profileRepository.findByUserId(UserUtils.getCurrentUser().getId());
        return postRepository.findAllByProfileId(profile.getId())
                .stream()
                .map(this::convertToPostDto)
                .collect(Collectors.toList());
    }

    @Override
    public void incrementLike(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            Integer like = post.getLikes();
            like++;
            post.setLikes(like);
            postRepository.save(post);
        }
    }

    private PostDto convertToPostDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        Profile profile = profileRepository.findByUserId(UserUtils.getCurrentUser().getId());
        dto.setUsername(UserUtils.getCurrentUser().getUsername());
        dto.setAliasProfile(profile.getAlias());
        if (profile.getAvatar() != null)
            dto.setAvatarId(profile.getAvatar().getId());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dto.setCreationDate(post.getCreationDate().format(dateTimeFormatter));
        dto.setLikes(post.getLikes());
        dto.setText(post.getText());
        dto.setComment(post.getComments());
        if (post.getImage() != null)
            dto.setImageId(post.getImage().getId());
        return dto;
    }

    private Image convertToImage(MultipartFile multipartFile) throws IOException {
        Image image = new Image();
        image.setName(multipartFile.getName());
        image.setOriginalFileName(multipartFile.getOriginalFilename());
        image.setSize(multipartFile.getSize());
        image.setContentType(multipartFile.getContentType());
        image.setImageBytes(multipartFile.getBytes());

        return image;
    }
}
