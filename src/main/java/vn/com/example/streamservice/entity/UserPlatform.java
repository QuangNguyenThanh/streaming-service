package vn.com.example.streamservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import vn.com.example.streamservice.enums.PlatformType;

@Entity
@Table(name = "user_platform")
@Data
public class UserPlatform{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    @Enumerated(EnumType.STRING)
    private PlatformType platformType;
    
    @Column(name = "is_active", columnDefinition = "tinyint(1) default 1")
    private boolean active;
    
    @Column(name = "is_deleted", columnDefinition = "tinyint(1) default 0")
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", insertable = true, updatable = true)
    private User user;

}
