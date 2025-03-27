package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.error.ErrorResponse;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

}
