package com.youtube.agent.services;

import com.youtube.agent.storages.youtubedata.YoutubeData;

import java.util.Optional;

public interface YouTubeDataService {

    Optional<YoutubeData> findLatestByUserId(Long userId);

    void save(YoutubeData youtubeData);
}
