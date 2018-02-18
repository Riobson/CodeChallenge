package com.hsbc.codechallenge.backend.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PostCreationDto {
    String message;
    String login;
}
