package com.hsbc.codechallenge.backend.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FollowedUserDto {
    Long id;

    public FollowedUserDto(BigDecimal id) {
        this.id = id.longValue();
    }

    public FollowedUserDto(BigInteger id) {
        this.id = id.longValue();
    }
}
