package com.vinfast.rental_service.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("RedisToken")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedisToken {
    private String id;
    private String accessToken;
    private String refreshToken;
    private String resetToken;
}
