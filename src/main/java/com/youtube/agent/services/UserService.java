package com.youtube.agent.services;

import com.youtube.agent.dto.UserPartialUpdate;
import com.youtube.agent.storages.user.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    User save(User user);
    void partialUpdate(Long id, UserPartialUpdate userPartialUpdate);
}
