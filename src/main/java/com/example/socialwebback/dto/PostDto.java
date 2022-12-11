package com.example.socialwebback.dto;

import com.example.socialwebback.model.Comment;
import lombok.Data;

import java.util.List;

@Data
public class PostDto {
    private Long id;
    private String username;
    private String aliasProfile;
    private Long avatarId;
    private Long imageId;
    private String text;
    private String creationDate;
    private Integer likes;
    private List<Comment> comment;
}
