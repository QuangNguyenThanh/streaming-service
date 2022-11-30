package vn.com.example.streamservice.entity;

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
@Table(name = "user_channel_youtube")
@Data
public class UserChannelYoutube{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String displayName;
    
    @Column
    private String imageUrl;

    @Column
    private String streamKey;

    @Column
    private String channelId;

    @Column
    private String backupIngestionAddress;
    
    @Column
    private String ingestionAddress;

    @Column
    private String streamId;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", insertable = true, updatable = true)
    private User user;

}
