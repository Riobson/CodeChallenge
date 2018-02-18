package com.hsbc.codechallenge.backend.services;

import com.hsbc.codechallenge.backend.dtos.PostCreationDto;
import com.hsbc.codechallenge.backend.dtos.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Page<PostDto> getUserWallPosts(Long id, Pageable pageable);

    Long addPost(PostCreationDto postCreationDto);

    Page<PostDto> getUserTimelinePosts(Long id, Pageable pageable);
}
