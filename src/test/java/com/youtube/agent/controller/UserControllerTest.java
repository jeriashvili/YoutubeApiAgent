package com.youtube.agent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youtube.agent.dto.UserPartialUpdate;
import com.youtube.agent.services.UserService;
import com.youtube.agent.services.YouTubeDataService;
import com.youtube.agent.storages.user.User;
import com.youtube.agent.storages.youtubedata.YoutubeData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@WithMockUser(username = "1")
public class UserControllerTest {

    //    @Autowired
    private MockMvc mockMvc;
    @Autowired
    public ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private YouTubeDataService youTubeDataService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private InOrder inOrder;
    public ArgumentCaptor<Long> userIdCapture = ArgumentCaptor.forClass(Long.class);


    @BeforeEach()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        inOrder = Mockito.inOrder(userService, youTubeDataService);
    }

    @Test
    public void success_register() throws Exception {
        User expectedUser = User.builder()
                .id(1L)
                .username("test")
                .password("testPass")
                .country("testCountry")
                .jobTimeInMinutes(1)
                .build();
        Mockito.when(userService.save(any())).thenReturn(expectedUser);
        User response = objectMapper.readValue(mockMvc.perform(
                        post("/api/users/register")
                                .content(objectMapper.writeValueAsBytes(User.builder()
                                        .username("test")
                                        .password("testPass")
                                        .country("testCountry")
                                        .jobTimeInMinutes(1)
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), User.class);
        assertEquals(response, expectedUser);
        inOrder.verify(userService).save(any());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void badRequest_register() throws Exception {
        mockMvc.perform(
                        post("/api/users/register")
                                .content(objectMapper.writeValueAsString(User.builder().build()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void success_partialUpdate() throws Exception {
        UserPartialUpdate userPartialUpdate = new UserPartialUpdate("test", 1);
        Mockito.doNothing().when(userService).partialUpdate(any(), any());
        mockMvc.perform(
                        patch("/api/users")
                                .content(objectMapper.writeValueAsString(userPartialUpdate))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        inOrder.verify(userService).partialUpdate(userIdCapture.capture(), any());
        inOrder.verifyNoMoreInteractions();
        assertEquals(userIdCapture.getValue(), 1L);
    }

    @Test
    public void badRequest_partialUpdate() throws Exception {
        UserPartialUpdate userPartialUpdate = new UserPartialUpdate(null, 1);
        Mockito.doNothing().when(userService).partialUpdate(any(), any());
        mockMvc.perform(
                        patch("/api/users")
                                .content(objectMapper.writeValueAsString(userPartialUpdate))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        inOrder.verifyNoMoreInteractions();
    }
    @Test
    public void success_getYoutubeData() throws Exception {
        YoutubeData ExpectedYoutubeData = YoutubeData.builder()
                .id(1L)
                .userId(1L)
                .videoId("testVideoId")
                .mostPopularVideo("testMostPopularVideo")
                .mostPopularComment("testMostPopularComment")
                .build();
        Mockito.when(youTubeDataService.findLatestByUserId(any())).thenReturn(Optional.ofNullable(ExpectedYoutubeData));
        YoutubeData response = objectMapper.readValue(mockMvc.perform(
                        get("/api/users/youtube-data")
                                .content(objectMapper.writeValueAsString(YoutubeData.builder()
                                        .videoId("testVideoId")
                                        .mostPopularVideo("testMostPopularVideo")
                                        .mostPopularComment("testMostPopularComment")
                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), YoutubeData.class);
        assertEquals(response, ExpectedYoutubeData);
        inOrder.verify(youTubeDataService).findLatestByUserId(any());
        inOrder.verifyNoMoreInteractions();
    }
}
