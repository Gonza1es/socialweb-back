package com.example.socialwebback.dto;

import lombok.Data;

@Data
public class AddComment {
    Long postId;
    String text;
}
