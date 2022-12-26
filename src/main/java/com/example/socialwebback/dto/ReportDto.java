package com.example.socialwebback.dto;

import lombok.Data;

@Data
public class ReportDto {
    private Long id;
    private String reporterUsername;
    private Long avatarId;
    private String aliasProfile;
    private String creationDate;
    private String text;
    private Long imageId;
}
