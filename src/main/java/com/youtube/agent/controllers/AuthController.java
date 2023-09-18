package com.youtube.agent.controllers;

import com.youtube.agent.exceptions.YoutubeAgentApiException;
import com.youtube.agent.services.UserService;
import com.youtube.agent.storages.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @GetMapping(path = "/authorize")
    public ResponseEntity<User> authorize() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findById(Long.valueOf(authentication.getName()));
        if (user.isEmpty()) {
            throw new YoutubeAgentApiException("User not found");
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }
}
