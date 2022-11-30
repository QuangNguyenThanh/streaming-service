package vn.com.example.streamservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.com.example.streamservice.entity.UserAuthTwitch;

public interface UserAuthTwitchRepository extends JpaRepository<UserAuthTwitch, Long> {

}
