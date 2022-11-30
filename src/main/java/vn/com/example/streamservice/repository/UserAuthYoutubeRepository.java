package vn.com.example.streamservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.com.example.streamservice.entity.UserAuthYoutube;

public interface UserAuthYoutubeRepository extends JpaRepository<UserAuthYoutube, Long> {

    Optional<UserAuthYoutube> findByUserIdAndUserDeletedFalse(Long userId);

    Optional<UserAuthYoutube> findByUserIdAndUserDeletedFalseAndDeletedFalse(Long id);
}
