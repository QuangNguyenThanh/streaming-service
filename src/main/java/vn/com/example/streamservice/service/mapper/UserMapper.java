package vn.com.example.streamservice.service.mapper;

import org.springframework.stereotype.Service;

import vn.com.example.streamservice.entity.User;
import vn.com.example.streamservice.service.dto.UserDto;

@Service
public class UserMapper {
    public UserDto toDto(User user) {
        if (user == null) return null;
        return UserDto
            .builder()
            .id(user.getId())
            .email(user.getEmail())
            .password(user.getPassword())
            .build();
    }
}
