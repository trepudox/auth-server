package com.trepudox.authserver.dataprovider.dto;

import com.trepudox.authserver.dataprovider.enums.EnProfile;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String username;
    private EnProfile profile;
    private Long score;
    private LocalDateTime updateDateTime;
    private LocalDateTime creationDateTime;

}
