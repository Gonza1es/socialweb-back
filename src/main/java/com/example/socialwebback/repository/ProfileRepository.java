package com.example.socialwebback.repository;

import com.example.socialwebback.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    void deleteByUserId(Long userId);
    Profile findByUserId(Long userId);
}
