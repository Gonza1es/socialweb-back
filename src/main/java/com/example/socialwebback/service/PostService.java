package com.example.socialwebback.service;

import com.example.socialwebback.dto.PostDto;
import com.example.socialwebback.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface PostService {


    void createPost(String text, MultipartFile file) throws IOException;

    List<PostDto> getPostCurrentProfile();

    void incrementLike(Long postId);
}
