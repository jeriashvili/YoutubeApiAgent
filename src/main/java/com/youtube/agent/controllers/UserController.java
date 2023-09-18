package com.youtube.agent.controllers;

import com.youtube.agent.dto.UserPartialUpdate;
import com.youtube.agent.services.UserService;
import com.youtube.agent.services.YouTubeDataService;
import com.youtube.agent.storages.user.User;
import com.youtube.agent.storages.youtubedata.YoutubeData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final YouTubeDataService youTubeDataService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @PatchMapping("")
    public ResponseEntity<Void> partialUpdate(@Valid @RequestBody UserPartialUpdate userPartialUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.partialUpdate(Long.valueOf(authentication.getName()), userPartialUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/youtube-data")
    public ResponseEntity<YoutubeData> getYouTubeData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<YoutubeData> youtubeDataOptional = youTubeDataService.findLatestByUserId(Long.valueOf(authentication.getName()));
        return new ResponseEntity<>(youtubeDataOptional.orElse(null), HttpStatus.OK);
    }
}
