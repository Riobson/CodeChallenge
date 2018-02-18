package com.hsbc.codechallenge.backend.services.impl;

import com.hsbc.codechallenge.backend.entities.User;
import com.hsbc.codechallenge.backend.exceptions.ApplicationException;
import com.hsbc.codechallenge.backend.repositories.UserRepository;
import com.hsbc.codechallenge.backend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ApplicationException("User with id " + userId + " does not exists."))
                ;
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new ApplicationException("User " + login + " does not exists."));
    }

    @Override
    public User addUser(String login) {
        log.info("Adding new user " + login);
        if (StringUtils.isEmpty(login)) {
            throw new RuntimeException("User login cannot be empty");
        }
        User user = new User();
        user.setLogin(login);
        return userRepository.save(user);
    }

    @Override
    public boolean exists(String login) {
        log.info("Checking if user exists " + login);
        return userRepository.existsByLogin(login);
    }

    @Override
    public void follow(Long followerId, Long followedId) {
        log.info("Add new folowed user " + followedId + " for user " + followerId);
        if (followerId == followedId) {
            throw new ApplicationException("User cannot follow himself");
        }
        User follower = findById(followerId);
        User followed = findById(followedId);
        Set<User> followers = follower.getFollowers();
        if (followers == null) {
            followers = new HashSet<>();
            followers.add(followed);
            follower.setFollowers(followers);
        } else {
            if (follower.getFollowers().contains(followed)) {
                throw new ApplicationException("User " + followed.getLogin() + " is already followed");
            }
            follower.getFollowers().add(followed);
        }
    }

    @Override
    public Set<Long> findUserFollowers(Long id) {
        log.info("Finding followers for user " + id);
        return userRepository.findFollowersByUserId(id).stream().map(followedUserDto -> followedUserDto.getId()).collect(Collectors.toSet());
    }
}
