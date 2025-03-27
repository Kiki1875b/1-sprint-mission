package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.error.ErrorResponse;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {

    ErrorResponse er = new ErrorResponse(
        ex.getTimestamp(),
        ex.getErrorCode().getCode(),
        ex.getMessage(),
        ex.getDetails(),
        ex.getClass().getSimpleName(),
        ex.getStatusCode()
    );

    return ResponseEntity.status(ex.getStatusCode()).body(er);
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {

    log.warn("[UNHANDLED ERROR]", ex);
    ErrorCode errorCode = ErrorCode.UNHANDLED_ERROR;

    ErrorResponse er = new ErrorResponse(
        Instant.now(),
        errorCode.getCode(),
        errorCode.getMessage(),
        Map.of("cause", "Uncaught Error"),
        ex.getClass().getSimpleName(),
        errorCode.getStatus().value()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
  }
}
