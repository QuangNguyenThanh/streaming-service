package vn.com.example.streamservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.com.example.streamservice.entity.UserPlatform;

public interface UserPlatformRepository extends JpaRepository<UserPlatform, Long> {

    Optional<UserPlatform> findByUserId(Long userId);

}
