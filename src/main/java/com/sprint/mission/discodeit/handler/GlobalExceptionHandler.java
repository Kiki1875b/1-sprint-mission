package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.error.ErrorDetail;
import com.sprint.mission.discodeit.error.ErrorResponse;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
    ErrorResponse er = new ErrorResponse(new ErrorDetail(ex.getStatusCode(), ex.getMessage()));
    return ResponseEntity.status(ex.getStatusCode()).body(er);
  }
}
