package com.trepudox.authserver.dataprovider.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {

    private String jwtToken;
    private String tokenType;
    private Long expiresIn;
    private List<String> roles;

}
