package com.example.socialwebback.repository;

import com.example.socialwebback.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByProfileId(Long profileId);

}
