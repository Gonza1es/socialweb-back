package com.example.socialwebback.repository;

import com.example.socialwebback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}
