package com.trepudox.authserver.core.exception.handler;

import com.trepudox.authserver.core.exception.APIException;
import com.trepudox.authserver.dataprovider.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@RestControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorResponseDTO> handleApiException(APIException e) {
        ErrorResponseDTO error = buildError(e.getTitle(), e.getDetail(), e.getStatus());

        log.error("{} - {}: {}", e.getClass().getSimpleName(), e.getTitle(), e.getDetail());

        return ResponseEntity.status(HttpStatus.valueOf(e.getStatus())).body(error);
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ErrorResponseDTO> handleRedisConnectionFailureException(RedisConnectionFailureException e) {
        String title = "Serviço fora do ar";
        String detail = "Não foi possível se conectar ao Redis";
        int status = 503;

        ErrorResponseDTO error = buildError(title, detail, status);
        log.error("Redis fora do ar! Message: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.valueOf(status)).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception e) {
        String title = "Não foi possível realizar a operação desejada";
        int status = 500;

        ErrorResponseDTO error = buildError(title, e.getMessage(), status);
        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.valueOf(status)).body(error);
    }

    private ErrorResponseDTO buildError(String title, String detail, int status) {
        return ErrorResponseDTO.builder()
                .title(title)
                .detail(detail)
                .status(status)
                .artifacts(new ArrayList<>())
                .dateTime(LocalDateTime.now())
                .build();
    }

}
