package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  @Override
  @Transactional
  public UserStatusResponseDto updateByUserId(String userId, UpdateUserStatusDto dto) {

    UserStatus status = userStatusRepository.findByUser_Id(UUID.fromString(userId)).orElseThrow(
        () -> new CustomException(ErrorCode.DEFAULT_ERROR_MESSAGE)
    );

    Instant updateTime = (dto.newLastActivityAt() == null) ? Instant.now() : dto.newLastActivityAt();

    status.updateLastOnline(updateTime);

    userStatusRepository.save(status);

    return new UserStatusResponseDto(
        status.getId().toString(),
        userId,
        updateTime
    );
  }
}
