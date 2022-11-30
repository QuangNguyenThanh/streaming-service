package vn.com.example.streamservice.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user_auth_youtube")
@Data
public class UserAuthYoutube{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "access_token")
    private String accessToken;
    
    @Column(name = "expires_session")
    private Instant expiresSession;
    
    @Column(name = "is_deleted", columnDefinition = "tinyint(1) default 0")
    private boolean deleted;
    
    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", insertable = true, updatable = true)
    private User user;

}
