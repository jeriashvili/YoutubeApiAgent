package com.youtube.agent.services.Impl;

import com.youtube.agent.services.SchedulerFactoryService;
import com.youtube.agent.services.YoutubeDataIntegrationService;
import com.youtube.agent.storages.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@Slf4j
@Service
public class SchedulerFactoryServiceImpl implements SchedulerFactoryService {

    private static final Map<Long, ScheduledFuture<?>> schedulers = new ConcurrentHashMap<>();

    private final TaskScheduler taskScheduler;
    private final YoutubeDataIntegrationService youtubeDataService;



    @Override
    public void create(User user) {
        log.info("Create scheduling on userId  -> {} started!!", user.getId());
        ScheduledFuture<?> scheduledFuture = schedulers.get(user.getId());
        if (!ObjectUtils.isEmpty(scheduledFuture)) {
            log.info("Cancel Scheduler on userId -> {} not found", user.getId());
            scheduledFuture.cancel(true);
        }
        schedulers.put(user.getId(), createScheduling(user));
        log.info("Created Scheduling on userId  -> {} finished!!", user.getId());
    }

    private ScheduledFuture<?> createScheduling(User user) {
        return taskScheduler.scheduleWithFixedDelay(() -> {
            youtubeDataService.callYoutubeServices(user.getId(), user.getCountry());
        }, Duration.ofMinutes(user.getJobTimeInMinutes()));
    }

}
