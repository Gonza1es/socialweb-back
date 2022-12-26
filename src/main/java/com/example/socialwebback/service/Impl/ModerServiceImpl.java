package com.example.socialwebback.service.Impl;

import com.example.socialwebback.dto.ReportDto;
import com.example.socialwebback.model.Post;
import com.example.socialwebback.model.Profile;
import com.example.socialwebback.model.Report;
import com.example.socialwebback.repository.PostRepository;
import com.example.socialwebback.repository.ProfileRepository;
import com.example.socialwebback.repository.ReportRepository;
import com.example.socialwebback.repository.UserRepository;
import com.example.socialwebback.service.ModerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModerServiceImpl implements ModerService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Autowired
    public ModerServiceImpl(ReportRepository reportRepository,
                            PostRepository postRepository,
                            ProfileRepository profileRepository,
                            UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ReportDto> getReports() {

        return reportRepository.findAll()
                .stream()
                .map(this::convertToReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReport(Long reportId) {
        reportRepository.deleteById(reportId);
    }

    @Override
    public void deletePost(Long reportId) {
        Long postId = reportRepository.findById(reportId).get().getPostId();
        reportRepository.deleteById(reportId);
        postRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public void deleteUser(Long reportId) {
        Long postId = reportRepository.findById(reportId).get().getPostId();
        reportRepository.deleteById(reportId);
        Long userId = postRepository.findById(postId).get().getProfile().getUserId();
        profileRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void deleteUserBy(Long userId) {
        profileRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    private ReportDto convertToReportDto(Report report) {
        ReportDto dto = new ReportDto();
        dto.setId(report.getId());
        dto.setReporterUsername(report.getReporterUsername());
        Post post = postRepository.findById(report.getPostId()).get();
        Profile profile = post.getProfile();
        dto.setAliasProfile(profile.getAlias());
        if (profile.getAvatar() != null)
            dto.setAvatarId(profile.getAvatar().getId());
        if (post.getImage() != null)
            dto.setImageId(post.getImage().getId());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dto.setCreationDate(post.getCreationDate().format(dateTimeFormatter));
        dto.setText(post.getText());

        return dto;
    }
}
