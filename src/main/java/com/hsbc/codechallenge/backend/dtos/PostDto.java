package com.hsbc.codechallenge.backend.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    Long id;
    String message;
    LocalDateTime date;
    Long userId;
    String userLogin;
}
