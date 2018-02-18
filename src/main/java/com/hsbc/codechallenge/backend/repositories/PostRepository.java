package com.hsbc.codechallenge.backend.repositories;

import com.hsbc.codechallenge.backend.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor {

    Page<Post> findByUserIdOrderByDateDesc(Long id, Pageable pageable);
}
