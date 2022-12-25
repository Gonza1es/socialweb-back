package com.example.socialwebback.dto;

public class AdminUserDto {
    private Long id;
    private String username;
    private String profileAlias;
    private Long avatarId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileAlias() {
        return profileAlias;
    }

    public void setProfileAlias(String profileAlias) {
        this.profileAlias = profileAlias;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }
}
