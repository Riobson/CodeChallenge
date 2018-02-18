package com.hsbc.codechallenge.backend.controllers;

import com.hsbc.codechallenge.backend.dtos.PostCreationDto;
import com.hsbc.codechallenge.backend.services.PostService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/posts")
public class PostController {

    PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Add new post for given user
     *
     * @param postCreationDto contains post message and login of the owner
     * @return post id
     */
    @PostMapping
    public Long addPost(@RequestBody PostCreationDto postCreationDto) {
        return postService.addPost(postCreationDto);
    }
}
