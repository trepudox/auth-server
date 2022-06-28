package com.trepudox.authserver.dataprovider.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

    private String title;
    private String detail;
    private List<ArtifactDTO> artifacts;
    private int status;
    private LocalDateTime dateTime;

}
