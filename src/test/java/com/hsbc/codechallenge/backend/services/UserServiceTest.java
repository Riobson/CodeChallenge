package com.hsbc.codechallenge.backend.services;

import com.hsbc.codechallenge.backend.entities.User;
import com.hsbc.codechallenge.backend.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TestTransaction;

import javax.transaction.Transactional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;


    private static final String EXISTING_USER_LOGIN = "EXISTING_LOGIN";
    private static final String SECOND_EXISTING_USER_LOGIN = "SECOND_EXISTING_LOGIN";
    private static final String NOT_EXISTING_USER_LOGIN = "USER_LOGIN";
    private static final String EMPTY_LOGIN = "";
    private static final Long NOT_EXISTING_USER_ID = -1l;


    @Before
    public void setUp() {
    }

    private User createUser(String login) {
        User user = new User();
        user.setLogin(login);
        return userRepository.save(user);
    }

    @Test
    public void GivenUserId_WhenCorrectlyFoundUser_ThenUserReturned() {
        Long userId = createUser(EXISTING_USER_LOGIN).getId();
        User user = userService.findById(userId);
        assertThat(user, is(notNullValue()));
        assertThat(user.getId(), equalTo(userId));
    }

    @Test(expected = RuntimeException.class)
    public void GivenUserId_WhenUserNotFound_ThenExceptionThrown() {
        userService.findById(NOT_EXISTING_USER_ID);
    }

    @Test
    public void GivenUserLogin_WhenCorrectlyAdded_ThenUserSavedInDatabase() {
        User user = userService.addUser(NOT_EXISTING_USER_LOGIN);
        assertThat(user, is(notNullValue()));
        assertThat(user.getLogin(), equalTo(NOT_EXISTING_USER_LOGIN));
    }

    @Test(expected = RuntimeException.class)
    public void GivenEmptyUserLogin_WhenTryToAddUser_ThenExceptionThrown() {
        userService.addUser(EMPTY_LOGIN);
    }

    @Test(expected = RuntimeException.class)
    public void GivenExistingUserLogin_WhenTryToAddUser_ThenExceptionThrown() {
        createUser(EXISTING_USER_LOGIN);
        userService.addUser(EXISTING_USER_LOGIN);
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    public void GivenUserLogin_WhenUserExists_ThenReturnTrue() {
        createUser(EXISTING_USER_LOGIN);
        assertThat(userService.exists(EXISTING_USER_LOGIN), is(true));
    }

    @Test
    public void GivenUserLogin_WhenUserNotExists_ThenReturnFalse() {
        assertThat(userService.exists(NOT_EXISTING_USER_LOGIN), is(false));
    }

    @Test
    public void GivenUserLogin_WhenCorrectlyFoundUser_ThenUserReturned() {
        createUser(EXISTING_USER_LOGIN);
        User user = userService.findByLogin(EXISTING_USER_LOGIN);
        assertThat(user, is(notNullValue()));
        assertThat(user.getLogin(), equalTo(EXISTING_USER_LOGIN));
    }

    @Test(expected = RuntimeException.class)
    public void GivenUserLogin_WhenUserNotFound_ThenExceptionThrown() {
        userService.findByLogin(NOT_EXISTING_USER_LOGIN);
    }

    @Test(expected = RuntimeException.class)
    public void GivenFollowerUserAndUserToBeFollowed_WhenUserWantToFollowHimself_ThenExceptionIsThrown() {
        Long followerId = createUser(EXISTING_USER_LOGIN).getId();
        userService.follow(followerId, followerId);
    }

    @Test()
    public void GivenFollowerUserAndUserToBeFollowed_WhenCorrectlyFollow_ThenUserHasOneFollowed() {
        Long followerId = createUser(EXISTING_USER_LOGIN).getId();
        Long followedId = createUser(SECOND_EXISTING_USER_LOGIN).getId();
        userService.follow(followerId, followedId);
        User user = userService.findById(followerId);
        assertThat(user.getFollowers(), is(not(empty())));
        assertThat(user.getFollowers().size(), is(1));
    }

    @Test(expected = RuntimeException.class)
    public void GivenFollowerUserAndUserToBeFollowed_WhenUserToBeFollowedNotExists_ThenExceptionIsThrown() {
        Long followerId = createUser(EXISTING_USER_LOGIN).getId();
        userService.follow(followerId, NOT_EXISTING_USER_ID);
    }

    @Test
    public void GivenUser_WhenUserHasFollowers_ThenReturnFollowersIds() {
        Long followerId = createUser(EXISTING_USER_LOGIN).getId();
        Long followedId = createUser(SECOND_EXISTING_USER_LOGIN).getId();
        userService.follow(followerId, followedId);
        Set<Long> followersIds = userService.findUserFollowers(followerId);
        assertThat(followersIds, is(not(empty())));
    }

    @Test
    public void GivenUser_WhenUserHasNoFollowers_ThenReturnEmptySet() {
        Long followerId = createUser(EXISTING_USER_LOGIN).getId();
        userService.findUserFollowers(followerId);
    }
}
