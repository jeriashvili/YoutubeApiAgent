package com.youtube.agent.services;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.youtube.agent.storages.youtubedata.YoutubeData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@RequiredArgsConstructor
@Service
@Slf4j
public class YoutubeDataIntegrationService {
    private static final String DEVELOPER_KEY = "AIzaSyCot5Y8QTN3oFi1m1BNIDv_JsotvZX8o3c";
    private static final String APPLICATION_NAME = "Youtube Agent";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final YouTubeDataService youTubeDataService;

    public void callYoutubeServices(Long userId, String country) {
        try {
            YouTube youtubeService = getService();
            YouTube.Videos.List request = youtubeService.videos()
                    .list(Collections.singletonList("statistics"));
            request.setKey(DEVELOPER_KEY);
            VideoListResponse response = request.setChart("mostPopular")
                    .setRegionCode(country)
                    .execute();
            Video video = response.getItems().stream()
                    .max((video1, video2) -> video1.getStatistics().getViewCount().compareTo(video2.getStatistics().getViewCount())).get();
            YouTube.CommentThreads.List commentRequest = youtubeService.commentThreads()
                    .list(Collections.singletonList("snippet"));
            CommentThreadListResponse commentResponse = commentRequest.setKey(DEVELOPER_KEY)
                    .setVideoId(video.getId())
                    .execute();
            CommentThread CommentThread = commentResponse.getItems().stream()
                    .max((comment1, comment2) -> comment1.getSnippet().getTopLevelComment().getSnippet().getLikeCount().compareTo(comment2.getSnippet().getTopLevelComment().getSnippet().getLikeCount())).get();
            youTubeDataService.save(YoutubeData.builder()
                    .userId(userId)
                    .videoId(video.getId())
                    .mostPopularVideo(buildYoutubeLink(video.getId()))
                    .mostPopularComment(CommentThread.getSnippet().getTopLevelComment().getSnippet().getTextDisplay())
                    .build());
        } catch (Exception e) {
            log.error("Error during calling Youtube API. Reason: ", e);
        }
    }

    private String buildYoutubeLink(String videoId) {
        return "https://www.youtube.com/watch?v=" + videoId;
    }

    private static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, httpRequest -> {
        }).setApplicationName(APPLICATION_NAME).build();
    }

//    public static void main(String[] args)
//            throws GeneralSecurityException, IOException, GoogleJsonResponseException {
//        YouTube youtubeService = getService();
//        YouTube.Videos.List request = youtubeService.videos()
//                .list(Collections.singletonList("statistics"));
//        request.setKey(DEVELOPER_KEY);
//        VideoListResponse response = request.setChart("mostPopular")
//                .setRegionCode("GE")
//                .execute();
//
//        Video video = response.getItems().stream()
//                .max((video1, video2) -> video1.getStatistics().getViewCount().compareTo(video2.getStatistics().getViewCount())).get();
//        System.out.println(video.getStatistics().getViewCount());
//        System.out.println(video.getId());
//
//        YouTube.CommentThreads.List commentRequest = youtubeService.commentThreads()
//                .list(Collections.singletonList("snippet"));
//        CommentThreadListResponse commentResponse = commentRequest.setKey(DEVELOPER_KEY)
//                .setVideoId(video.getId())
//                .execute();
//        CommentThread CommentThread = commentResponse.getItems().stream()
//                .max((comment1, comment2) -> comment1.getSnippet().getTopLevelComment().getSnippet().getLikeCount().compareTo(comment2.getSnippet().getTopLevelComment().getSnippet().getLikeCount())).get();
//        System.out.println(CommentThread.getSnippet().getTopLevelComment().getSnippet().getTextDisplay());
//    }
}