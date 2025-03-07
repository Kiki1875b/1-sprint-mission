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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;


  @Override
  public UserStatusResponseDto updateByUserId(String userId, UpdateUserStatusDto dto) {

    UserStatus status = userStatusRepository.findByUser_Id(UUID.fromString(userId)).orElseThrow(
        () -> new CustomException(ErrorCode.DEFAULT_ERROR_MESSAGE)
    );

    User user = status.getUser();

    status.updateLastOnline(dto.newLastActivityAt());

    userStatusRepository.save(status);

    return userMapper.withStatus(user);
  }
}
