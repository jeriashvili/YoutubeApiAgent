package com.youtube.agent.storages.youtubedata;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface YoutubeDataRepository extends JpaRepository<YoutubeData, Long> {
    Optional<YoutubeData> findTopByUserIdOrderByIdDesc(Long userId);
}
