package com.example.socialwebback.dto;

import lombok.Data;

@Data
public class CommentDto {
    private String profileAlias;
    private String text;
    private Long avatarId;
    private String creationDate;
}
