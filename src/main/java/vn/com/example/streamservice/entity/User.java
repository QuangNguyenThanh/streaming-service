package vn.com.example.streamservice.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "is_deleted", columnDefinition = "tinyint(1) default 0")
    private boolean deleted;

    @OneToMany(mappedBy = "user")
    private List<UserAuthYoutube> userAuthYoutubes;

    @OneToMany(mappedBy = "user")
    private Set<UserSession> sessions;
    
    @OneToMany(mappedBy = "user")
    private List<UserAuthTwitch> userAuthTwitchs;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserChannelYoutube> userChannelYoutubes;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserChannelTwitch> userChannelTwitchs;
    
    @OneToMany(mappedBy = "user")
    private List<UserPlatform> userPlatforms;

}
