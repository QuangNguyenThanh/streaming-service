package vn.com.example.streamservice.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.example.streamservice.security.dto.JwtTokenDto;

@Data
@Entity
@Table(name = "t_user_session")
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id")
    private String sessionId;

    @Lob
    @Column(name = "token")
    private String token;

    @Column(name = "expired_time")
    private Date expiredTime;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User user;

    public UserSession(User user, JwtTokenDto tokenDto) {
        sessionId = tokenDto.getTokenId();
        token = tokenDto.getToken();
        expiredTime = tokenDto.getExpiredTime();
        this.user = user;
    }
}
