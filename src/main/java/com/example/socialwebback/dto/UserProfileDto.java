package com.example.socialwebback.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private String aliasProfile;
    private String status;
    private Long avatarId;
    private Long coverId;
    private int subscribersCount;
    private Boolean isActiveUser;
    private Boolean isSubscribed;
}
