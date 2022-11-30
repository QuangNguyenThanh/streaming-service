package vn.com.example.streamservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.com.example.streamservice.entity.UserChannelTwitch;

public interface UserChannelTwitchRepository extends JpaRepository<UserChannelTwitch, Long> {

    Optional<UserChannelTwitch> findByUserId(Long userId);

}
