package com.youtube.agent.services.Impl;

import com.youtube.agent.dto.UserPartialUpdate;
import com.youtube.agent.exceptions.YoutubeAgentApiException;
import com.youtube.agent.services.SchedulerFactoryService;
import com.youtube.agent.services.UserService;
import com.youtube.agent.storages.user.User;
import com.youtube.agent.storages.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final SchedulerFactoryService schedulerFactoryService;

    @PostConstruct
    public void initSchedulers() {
        userRepository.findAll().forEach(schedulerFactoryService::create);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            throw new YoutubeAgentApiException("User with this username -> " + user.getUsername() + " is already exists!!");
        }
        user.setCreationDate(LocalDateTime.now());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        schedulerFactoryService.create(user);
        return user;
    }

    @Override
    public void partialUpdate(Long id, UserPartialUpdate userPartialUpdate) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new YoutubeAgentApiException("User not found");
        }
        User prevUser = userOptional.get();
        if (prevUser.getCountry().equals(userPartialUpdate.country()) && prevUser.getJobTimeInMinutes().equals(userPartialUpdate.jobTimeInMinutes())) {
            log.info("User was not changed -> {}", id);
            return;
        }
        prevUser.setCountry(userPartialUpdate.country());
        prevUser.setJobTimeInMinutes(userPartialUpdate.jobTimeInMinutes());
        userRepository.save(prevUser);
        schedulerFactoryService.create(prevUser);
    }
}
