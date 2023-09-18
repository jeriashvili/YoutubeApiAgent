package com.youtube.agent.services.Impl;

import com.youtube.agent.services.YouTubeDataService;
import com.youtube.agent.storages.youtubedata.YoutubeData;
import com.youtube.agent.storages.youtubedata.YoutubeDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class YouTubeDataServiceImpl implements YouTubeDataService {

    private final YoutubeDataRepository youtubeDataRepository;


    @Override
    public Optional<YoutubeData> findLatestByUserId(Long userId) {
        return youtubeDataRepository.findTopByUserIdOrderByIdDesc(userId);
    }

    @Override
    public void save(YoutubeData youtubeData) {
        youtubeDataRepository.save(youtubeData);
    }
}
