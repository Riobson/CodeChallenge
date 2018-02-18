package com.hsbc.codechallenge.backend.repositories;

import com.hsbc.codechallenge.backend.dtos.FollowedUserDto;
import com.hsbc.codechallenge.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLogin(String login);

    Optional<User> findByLogin(String login);

    Set<FollowedUserDto> findFollowersByUserId(@Param("id") Long id);
}
