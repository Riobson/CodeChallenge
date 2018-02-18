package com.hsbc.codechallenge.backend.assemblers;

import com.hsbc.codechallenge.backend.dtos.PostCreationDto;
import com.hsbc.codechallenge.backend.dtos.PostDto;
import com.hsbc.codechallenge.backend.entities.Post;
import com.hsbc.codechallenge.backend.entities.User;
import com.hsbc.codechallenge.backend.services.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostAssembler {
    UserService userService;

    public PostAssembler(UserService userService) {
        this.userService = userService;
    }

    public PostDto fromPost(Post post) {
        if (post == null) {
            return null;
        }
        return PostDto.builder()
                .id(post.getId())
                .message(post.getMessage())
                .date(post.getDate())
                .userId(post.getUser().getId())
                .userLogin(post.getUser().getLogin())
                .build();
    }

    public Post fromPostCreationDto(PostCreationDto postCreationDto) {
        if (postCreationDto == null) {
            return null;
        }
        User user;
        if (userService.exists(postCreationDto.getLogin())) {
            user = userService.findByLogin(postCreationDto.getLogin());
        } else {
            user = userService.addUser(postCreationDto.getLogin());
        }
        return Post.builder().message(postCreationDto.getMessage()).date(LocalDateTime.now()).user(user).build();
    }
}
