package com.hsbc.codechallenge.backend.specyfication;

import com.hsbc.codechallenge.backend.entities.Post;
import com.hsbc.codechallenge.backend.entities.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.*;
import java.util.HashSet;
import java.util.Set;

public class PostSpecyfication {

    public static Specification<Post> followersPosts(Set<Long> followersIds) {
        return new Specification<Post>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Set<Predicate> predicates = new HashSet<>();
                if (followersIds != null) {
                    Set<Predicate> usersPredicates = new HashSet<>();
                    followersIds.stream().forEach(id -> {
                        Join<Post, User> userJoin = root.join("user");
                        Predicate userPredicate = criteriaBuilder.equal(userJoin.get("id"), id);
                        usersPredicates.add(userPredicate);
                    });
                    predicates.add(criteriaBuilder.or(usersPredicates.toArray(new Predicate[usersPredicates.size()])));
                }
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("date")));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
