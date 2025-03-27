package com.sprint.mission.discodeit.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  UNHANDLED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER-001", "알 수 없는 오류입니다."),
  DEFAULT_ERROR_MESSAGE(HttpStatus.BAD_REQUEST, "GENERAL-001", "허용되지 않은 작업입니다."),
  INVALID_UUID_FORMAT(HttpStatus.BAD_REQUEST, "GENERAL-002", "UUID 형식이 올바르지 않습니다."),

  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "CHANNEL-001", "채널을 찾지 못했습니다."),
  NO_ACCESS_TO_CHANNEL(HttpStatus.UNAUTHORIZED, "CHANNEL-002", "접근 권한이 없는 채널입니다."),
  PRIVATE_CHANNEL_CANNOT_BE_UPDATED(HttpStatus.FORBIDDEN, "CHANNEL-003", "private 채널은 수정할 수 없습니다."),

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "사용자를 찾을 수 없습니다."),
  PASSWORD_MATCH_ERROR(HttpStatus.UNAUTHORIZED, "USER-002", "비밀번호가 일치하지 않습니다."),

  FILE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-001", "파일 오류 입니다."),
  ERROR_WHILE_DOWNLOADING(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-002", "파일 다운로드에 실패했습니다."),
  IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE-003", "이미지를 찾을 수 없습니다."),

  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE-001", "메시지를 찾을 수 없습니다."),

  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "READ-STATUS-001", "읽음 상태를 찾을 수 없습니다."),

  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-STATUS-001", "사용자 상태를 찾을 수 없습니다.");


  private final HttpStatus status;
  private final String code;
  private final String message;
}
