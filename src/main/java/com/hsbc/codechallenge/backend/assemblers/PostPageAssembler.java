package com.hsbc.codechallenge.backend.assemblers;

import com.hsbc.codechallenge.backend.dtos.PostDto;
import com.hsbc.codechallenge.backend.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostPageAssembler {
    PostAssembler postAssembler;

    public PostPageAssembler(PostAssembler postAssembler) {
        this.postAssembler = postAssembler;
    }

    public Page<PostDto> fromPostPage(Page<Post> posts) {
        if (posts == null) {
            return null;
        }
        List<PostDto> postDtos = new ArrayList<>();
        posts.stream().forEach(post -> postDtos.add(postAssembler.fromPost(post)));
        return new PageImpl<>(postDtos, posts.getPageable(), postDtos.size());
    }
}
