package com.hsbc.codechallenge.backend.services;

import com.hsbc.codechallenge.backend.dtos.PostCreationDto;
import com.hsbc.codechallenge.backend.dtos.PostDto;
import com.hsbc.codechallenge.backend.entities.Post;
import com.hsbc.codechallenge.backend.entities.User;
import com.hsbc.codechallenge.backend.repositories.PostRepository;
import com.hsbc.codechallenge.backend.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TestTransaction;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private static final String USER_LOGIN = "USER_LOGIN";
    private static final String SECOND_USER_LOGIN = "SECOND_USER_LOGIN";
    private static final String EXAMPLE_MESSAGE = "Example message";
    private static final String EMPTY_MESSAGE = "";
    private static final String TOO_LONG_MESSAGE = "12345678910123456789101234567891012345678910" +
                                                   "12345678910123456789101234567891012345678910" +
                                                   "12345678910123456789101234567891012345678910" +
                                                   "12345678910123456789101234567891012345678910";


    @Before
    public void setUp() {
    }

    private User createUser(String login) {
        User user = new User();
        user.setLogin(login);
        return userRepository.save(user);
    }

    private Post createPost(User user, String message) {
        Post post = new Post();
        post.setUser(user);
        post.setDate(LocalDateTime.now());
        post.setMessage(message);
        return postRepository.save(post);
    }

    private PostCreationDto createPostCreationDto(String login, String message) {
        return PostCreationDto.builder().login(login).message(message).build();
    }

    @Test
    public void GivenUser_WhenHasWallPosts_ThenReturnPageOfPosts() {
        User user = createUser(USER_LOGIN);
        createPost(user, EXAMPLE_MESSAGE);
        Page<PostDto> wallPosts = postService.getUserWallPosts(user.getId(), PageRequest.of(0, 20));
        assertThat(wallPosts, is(notNullValue()));
        assertThat(wallPosts.getContent().size(), is(1));
    }

    @Test
    public void GivenUser_WhenHasNoWallPosts_ThenReturnEmptyPage() {
        User user = createUser(USER_LOGIN);
        Page<PostDto> wallPosts = postService.getUserWallPosts(user.getId(), PageRequest.of(0, 20));
        assertThat(wallPosts, is(notNullValue()));
        assertThat(wallPosts.getContent().size(), is(0));
    }

    @Test
    public void GivenUser_WhenHasTimelinePosts_ThenReturnPageOfPosts() {
        User user = createUser(USER_LOGIN);
        User secondUser = createUser(SECOND_USER_LOGIN);
        createPost(secondUser, EXAMPLE_MESSAGE);
        userService.follow(user.getId(), secondUser.getId());
        Page<PostDto> timelinePosts = postService.getUserTimelinePosts(user.getId(), PageRequest.of(0, 20));
        assertThat(timelinePosts, is(notNullValue()));
        assertThat(timelinePosts.getContent().size(), is(1));
    }

    @Test
    public void GivenUser_WhenHasNoTimelinePosts_ThenReturnEmptyPage() {
        User user = createUser(USER_LOGIN);
        Page<PostDto> timelinePosts = postService.getUserTimelinePosts(user.getId(), PageRequest.of(0, 20));
        assertThat(timelinePosts, is(notNullValue()));
        assertThat(timelinePosts.getContent().size(), is(0));
    }

    @Test
    public void GivenPostCreationDto_WhenCorretlyAddPost_ThenReturnPostId() {
        PostCreationDto postCreationDto = createPostCreationDto(USER_LOGIN, EXAMPLE_MESSAGE);
        Long postId = postService.addPost(postCreationDto);
        assertThat(postId, is(notNullValue()));
    }

    @Test(expected = RuntimeException.class)
    public void GivenPostCreationDto_WhenMessageTooLong_ThenThrowException() {
        PostCreationDto postCreationDto = createPostCreationDto(USER_LOGIN, TOO_LONG_MESSAGE);
        postService.addPost(postCreationDto);
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test(expected = RuntimeException.class)
    public void GivenPostCreationDto_WhenMessageEmpty_ThenThrowException() {
        PostCreationDto postCreationDto = createPostCreationDto(EMPTY_MESSAGE, TOO_LONG_MESSAGE);
        postService.addPost(postCreationDto);
    }

    @Test
    public void GivenPostCreationDto_WhenUserNotExists_ThenUserCreated() {
        PostCreationDto postCreationDto = createPostCreationDto(USER_LOGIN, EXAMPLE_MESSAGE);
        postService.addPost(postCreationDto);
        User user = userService.findByLogin(USER_LOGIN);
        assertThat(user, is(notNullValue()));
    }


}
