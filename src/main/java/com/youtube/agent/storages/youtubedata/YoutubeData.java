package com.youtube.agent.storages.youtubedata;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@Builder
@Entity
public class YoutubeData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long userId;
    private String videoId;
    private String mostPopularVideo;
    @Column(length = 2000)
    private String mostPopularComment;
}
