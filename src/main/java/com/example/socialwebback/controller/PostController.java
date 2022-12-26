package com.example.socialwebback.controller;

import com.example.socialwebback.dto.AddComment;
import com.example.socialwebback.dto.CommentDto;
import com.example.socialwebback.dto.PostDto;
import com.example.socialwebback.model.Comment;
import com.example.socialwebback.model.Post;
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

    @GetMapping("/{username}")
    public List<PostDto> getUserPosts(@PathVariable String username) {
        return postService.getUserPosts(username);
    }

    @GetMapping("/feed")
    public List<PostDto> getFeed() {
        return postService.getPostsForFeed();
    }

    @PostMapping(value = "/addComment")
    public void addComment(@RequestBody AddComment addComment) {
        postService.createComment(addComment.getPostId(), addComment.getText());
    }

    @GetMapping(value = "/comment/{postId}")
    public List<CommentDto> getComments(@PathVariable Long postId) {
        return postService.getComments(postId);
    }

    @GetMapping(value = "/report/{postId}")
    public void createReport(@PathVariable Long postId) {
        postService.createReport(postId);
    }
}
