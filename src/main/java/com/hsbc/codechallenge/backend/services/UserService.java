package com.hsbc.codechallenge.backend.services;

import com.hsbc.codechallenge.backend.entities.User;

import java.util.Set;

public interface UserService {
    User findById(Long userId);

    User addUser(String login);

    boolean exists(String login);

    User findByLogin(String login);

    void follow(Long followerId, Long followedId);

    Set<Long> findUserFollowers(Long id);
}
