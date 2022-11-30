package vn.com.example.streamservice.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.com.example.streamservice.config.StreamProperties;
import vn.com.example.streamservice.entity.User;
import vn.com.example.streamservice.entity.UserSession;
import vn.com.example.streamservice.exception.BadRequestException;
import vn.com.example.streamservice.exception.UnauthorizedException;
import vn.com.example.streamservice.repository.UserRepository;
import vn.com.example.streamservice.repository.UserSessionRepository;
import vn.com.example.streamservice.security.JwtProvider;
import vn.com.example.streamservice.security.dto.JwtTokenDto;
import vn.com.example.streamservice.service.AuthService;
import vn.com.example.streamservice.service.RedisService;
import vn.com.example.streamservice.service.dto.ResetPasswordDto;
import vn.com.example.streamservice.service.dto.UserDto;
import vn.com.example.streamservice.service.dto.request.LoginDto;
import vn.com.example.streamservice.service.dto.request.SignupDto;
import vn.com.example.streamservice.service.dto.response.LoginResultDto;
import vn.com.example.streamservice.service.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    /*** Repositories */
    private final UserSessionRepository sessionRepository;
    private final UserRepository userRepository;

    /** Services */
    private final RedisService redisService;

    /** Others */
    private final StreamProperties properties;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;

    @Override
    public void logout(String sessionId) {
        Optional<UserSession> userSession = sessionRepository.findBySessionId(sessionId);
        if (userSession.isEmpty())
            return;

        sessionRepository.delete(userSession.get());
        pushSessionIdToBlacklist(sessionId);
    }

    @Override
    public LoginResultDto login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(UnauthorizedException::new);

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException();
        }

        JwtTokenDto tokenDto = jwtProvider.createToken(user);
        UserSession session = saveUserSession(user, tokenDto);
        return LoginResultDto.builder().token(tokenDto.getToken()).sessionId(session.getSessionId()).build();
    }

    @Override
    @Transactional
    public UserDto signup(SignupDto signupDto) {
        boolean isExistingEmail = userRepository.findByEmail(signupDto.getEmail()).isPresent();
        if (isExistingEmail) {
            throw new BadRequestException("Email already exist.");
        }
        User user = new User();
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        return userMapper.toDto(userRepository.save(user));
    }

    private UserSession saveUserSession(User user, JwtTokenDto tokenDto) {
        UserSession session = new UserSession(user, tokenDto);
        return sessionRepository.save(session);
    }

    private void pushSessionIdToBlacklist(String sessionId) {
        redisService.setData(String.format("blacklist:session:%s", sessionId), Boolean.TRUE,
                properties.getJwt().getTokenValidityInSeconds());
    }

    @Override
    @Transactional
    public Boolean resetPassword(ResetPasswordDto dto) {
        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if (user.isEmpty())
            return false;
        user.get().setPassword(passwordEncoder.encode(dto.getPassword()));
        return true;
    }
}
