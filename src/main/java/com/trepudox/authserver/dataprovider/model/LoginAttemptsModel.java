package com.trepudox.authserver.dataprovider.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "LA", timeToLive = 300)
public class LoginAttemptsModel {

    @Id
    private String username;
    private Long currentAttempt;

}
