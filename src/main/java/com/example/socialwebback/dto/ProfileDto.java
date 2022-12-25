package com.example.socialwebback.dto;


import lombok.Data;

@Data
public class ProfileDto {

    private String aliasProfile;
    private String status;
    private Long avatarId;
    private Long coverId;
    private Boolean isActiveUser;
    private int subscribersCount;
}
