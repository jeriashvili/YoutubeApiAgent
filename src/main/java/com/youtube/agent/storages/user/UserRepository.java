package com.youtube.agent.storages.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<com.youtube.agent.storages.user.User, Long> {
    Optional<com.youtube.agent.storages.user.User> findByUsername(String username);

}
