package com.example.socialwebback.service;


import com.example.socialwebback.dto.ReportDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ModerService {
    List<ReportDto> getReports();

    void deleteReport(Long reportId);

    void deletePost(Long reportId);

    void deleteUser(Long reportId);

    void deleteUserBy(Long userId);
}
