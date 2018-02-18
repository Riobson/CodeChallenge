package com.hsbc.codechallenge.backend.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "post_seq")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    private Long id;

    @Size(min = 1, max = 140)
    private String message;

    private LocalDateTime date;

    @ManyToOne
    private User user;
}
