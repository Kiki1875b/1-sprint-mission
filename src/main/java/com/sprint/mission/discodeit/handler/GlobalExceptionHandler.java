package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.error.ErrorResponse;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {
    Map<String, Object> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

    ErrorResponse er = new ErrorResponse(
        Instant.now(),
        errorCode.getCode(),
        errorCode.getMessage(),
        errors,
        ex.getClass().getSimpleName(),
        errorCode.getStatus().value()
    );

    return ResponseEntity.status(er.getStatus()).body(er);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    Map<String, Object> errors = ex.getConstraintViolations().stream()
        .collect(Collectors.toMap(
            violation -> violation.getPropertyPath().toString(),
            violation -> violation.getMessage(),
            (existing, replacement) -> existing
        ));

    ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

    ErrorResponse er = new ErrorResponse(
        Instant.now(),
        errorCode.getCode(),
        errorCode.getMessage(),
        errors,
        ex.getClass().getSimpleName(),
        errorCode.getStatus().value()
    );

    return ResponseEntity.status(er.getStatus()).body(er);
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

    return ResponseEntity.status(er.getStatus()).body(er);
  }
}
