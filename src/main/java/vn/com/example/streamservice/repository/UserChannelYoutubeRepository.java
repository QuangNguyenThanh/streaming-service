package vn.com.example.streamservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.com.example.streamservice.entity.UserChannelYoutube;

public interface UserChannelYoutubeRepository extends JpaRepository<UserChannelYoutube, Long> {

    Optional<UserChannelYoutube> findByChannelIdAndUserId(String channelId, Long userId);

    List<UserChannelYoutube> findByUserId(Long userId);

}
