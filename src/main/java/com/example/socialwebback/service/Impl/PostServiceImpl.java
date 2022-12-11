package com.example.socialwebback.service.Impl;

import com.example.socialwebback.dto.CommentDto;
import com.example.socialwebback.dto.PostDto;
import com.example.socialwebback.model.*;
import com.example.socialwebback.repository.PostRepository;
import com.example.socialwebback.repository.ProfileRepository;
import com.example.socialwebback.repository.UserRepository;
import com.example.socialwebback.service.PostService;
import com.example.socialwebback.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private ProfileRepository profileRepository;

    private PostRepository postRepository;

    private UserRepository userRepository;

    @Autowired
    public PostServiceImpl(ProfileRepository profileRepository, PostRepository postRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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

    @Override
    public List<PostDto> getUserPosts(String username) {
        User user = userRepository.findByUsername(username);
        Profile profile = profileRepository.findByUserId(user.getId());
        List<Post> posts = postRepository.findAllByProfileId(profile.getId());
        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            PostDto dto = convertToPostDtoForUser(post, profile, username);
            postDtos.add(dto);
        }
        return postDtos;
    }

    @Override
    public List<PostDto> getPostsForFeed() {
        Profile profile = profileRepository.findByUserId(UserUtils.getCurrentUser().getId());
        List<PostDto> postsCurrentUser = getPostCurrentProfile();
        List<PostDto> postsSubscriptions = new ArrayList<>();
        if (profile.getSubscriptions().size() != 0){
            for (Profile profileSubs: profile.getSubscriptions()) {
                List<PostDto> postDtos = getPostFeed(profileSubs);
                postsSubscriptions.addAll(postDtos);
            }
            postsCurrentUser.addAll(postsSubscriptions);
        }
        return postsCurrentUser;
    }

    @Override
    public void createComment(Long postId, String text) {
        Post post = postRepository.findById(postId).orElse(null);
        Comment comment = new Comment();
        comment.setText(text);
        post.addComment(comment);

        postRepository.save(post);
    }

    @Override
    public List<CommentDto> getComments(Long postId) {
        return null;
    }

    private List<PostDto> getPostFeed(Profile profile) {
        User user = userRepository.findById(profile.getUserId()).orElse(null);
        List<Post> posts = postRepository.findAllByProfileId(profile.getId());
        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            PostDto dto = convertToPostDtoForUser(post, profile, user.getUsername());
            postDtos.add(dto);
        }
        return postDtos;
    }

    private PostDto convertToPostDtoForUser(Post post, Profile profile, String username) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setUsername(username);
        dto.setAliasProfile(profile.getAlias());
        if (profile.getAvatar() != null)
            dto.setAvatarId(profile.getAvatar().getId());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dto.setCreationDate(post.getCreationDate().format(dateTimeFormatter));
        dto.setLikes(post.getLikes());
        dto.setText(post.getText());
        dto.setCommentsCount(post.getComments().size());
        if (post.getImage() != null)
            dto.setImageId(post.getImage().getId());
        return dto;
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
        dto.setCommentsCount(post.getComments().size());
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
