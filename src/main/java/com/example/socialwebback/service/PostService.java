package com.example.socialwebback.service;

import com.example.socialwebback.dto.CommentDto;
import com.example.socialwebback.dto.PostDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface PostService {


    void createPost(String text, MultipartFile file) throws IOException;

    List<PostDto> getPostCurrentProfile();

    void incrementLike(Long postId);

    List<PostDto> getUserPosts(String username);

    List<PostDto> getPostsForFeed();

    void createComment(Long postId, String text);

    List<CommentDto> getComments(Long postId);

    void createReport(Long postId);
}
