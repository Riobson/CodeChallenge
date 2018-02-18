package com.hsbc.codechallenge.backend.services.impl;

import com.hsbc.codechallenge.backend.assemblers.PostAssembler;
import com.hsbc.codechallenge.backend.assemblers.PostPageAssembler;
import com.hsbc.codechallenge.backend.dtos.PostCreationDto;
import com.hsbc.codechallenge.backend.dtos.PostDto;
import com.hsbc.codechallenge.backend.entities.Post;
import com.hsbc.codechallenge.backend.repositories.PostRepository;
import com.hsbc.codechallenge.backend.services.PostService;
import com.hsbc.codechallenge.backend.services.UserService;
import com.hsbc.codechallenge.backend.specyfication.PostSpecyfication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private PostPageAssembler postPageAssembler;
    private PostAssembler postAssembler;
    private UserService userService;

    public PostServiceImpl(PostRepository postRepository, PostPageAssembler postPageAssembler, PostAssembler postAssembler, UserService userService) {
        this.postRepository = postRepository;
        this.postPageAssembler = postPageAssembler;
        this.postAssembler = postAssembler;
        this.userService = userService;
    }

    @Override
    public Page<PostDto> getUserWallPosts(Long id, Pageable pageable) {
        log.info("Getting posts for user " + id);
        Page<Post> posts = postRepository.findByUserIdOrderByDateDesc(id, pageable);
        log.info("Found " + posts.getTotalElements() + " posts");
        return postPageAssembler.fromPostPage(posts);
    }

    @Override
    public Long addPost(PostCreationDto postCreationDto) {
        log.info("Adding new post for user " + postCreationDto.getLogin());
        Post post = postAssembler.fromPostCreationDto(postCreationDto);
        return postRepository.save(post).getId();
    }

    @Override
    public Page<PostDto> getUserTimelinePosts(Long id, Pageable pageable) {
        log.info("Getting followers posts for user " + id);
        Set<Long> followersIds = userService.findUserFollowers(id);
        List<Post> posts = postRepository.findAll(PostSpecyfication.followersPosts(followersIds));
        log.info("Found " + posts.size() + " posts");
        Page<Post> result = new PageImpl<>(posts, pageable, posts.size());
        return postPageAssembler.fromPostPage(result);
    }
}
