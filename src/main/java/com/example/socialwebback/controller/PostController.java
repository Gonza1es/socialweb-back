package com.example.socialwebback.controller;

import com.example.socialwebback.dto.PostDto;
import com.example.socialwebback.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/post")
public class PostController {


    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/createPost", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void createPost(@RequestPart String text,
                           @RequestPart(required = false) MultipartFile file) throws IOException {
        postService.createPost(text, file);
    }

    @GetMapping("/current")
    public List<PostDto> getPostsCurrentUser() {
        return postService.getPostCurrentProfile();
    }

    @GetMapping("/like/{postId}")
    public void incrementLike(@PathVariable Long postId) {

        postService.incrementLike(postId);
    }
}
