package com.hsbc.codechallenge.backend.controllers;

import com.hsbc.codechallenge.backend.dtos.FollowedUserDto;
import com.hsbc.codechallenge.backend.dtos.PostDto;
import com.hsbc.codechallenge.backend.services.PostService;
import com.hsbc.codechallenge.backend.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    private UserService userService;
    private PostService postService;

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    /**
     * Show list of all posts for given user
     *
     * @param id       unique user identifier
     * @param pageable specification of result page
     * @return page with user posts
     */
    @GetMapping("/{id}/wall")
    public Page<PostDto> getUserWallPosts(@PathVariable Long id, Pageable pageable) {
        return postService.getUserWallPosts(id, pageable);
    }

    /**
     * Show list of all followers posts for given user
     *
     * @param id       unique user identifier
     * @param pageable specification of result page
     * @return page with followers posts
     */
    @GetMapping("/{id}/timeline")
    public Page<PostDto> getUserTimelinePosts(@PathVariable Long id, Pageable pageable) {
        return postService.getUserTimelinePosts(id, pageable);
    }

    /**
     * Add new follower for given user
     *
     * @param id              unique user identifier
     * @param followedUserDto contains unique followed user identifier
     */
    @PatchMapping("/{id}/follow")
    public void followUser(@PathVariable Long id, @RequestBody FollowedUserDto followedUserDto) {
        userService.follow(id, followedUserDto.getId());
    }
}


