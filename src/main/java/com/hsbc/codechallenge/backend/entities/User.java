package com.hsbc.codechallenge.backend.entities;

import com.hsbc.codechallenge.backend.dtos.FollowedUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;


@SqlResultSetMapping(
        name = "Followers",
        classes = @ConstructorResult(
                targetClass = FollowedUserDto.class,
                columns = {
                        @ColumnResult(name = "id")
                }
        )
)

@NamedNativeQuery(name = User.FIND_FOLLOWERS_BY_USER_ID, query = "SELECT FOLLOWERS_ID AS ID FROM USER_FOLLOWERS WHERE USER_ID = :id", resultSetMapping = "Followers")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "user_seq")
public class User {

    static final String FIND_FOLLOWERS_BY_USER_ID = "User.findFollowersByUserId";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    @Column(unique = true)
    private String login;

    @OneToMany(mappedBy = "user")
    private Set<Post> posts;

    @OneToMany
    private Set<User> followers;
}
