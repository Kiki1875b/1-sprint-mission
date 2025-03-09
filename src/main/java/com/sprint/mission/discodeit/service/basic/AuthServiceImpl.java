package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserStatusService userStatusService;
  private final UserMapper userMapper;

  /**
   * FETCH JOIN 으로 user 불러올 때, 관련 BinaryContent, UserStatus 불러온 후
   * 온라인시간 업데이트
   */
  @Override
  @Transactional
  public UserResponseDto login(String username, String password) {

    User targetUser = userRepository.findByUsernameWithProfileAndStatus(username)
        .orElseThrow(
            () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

    if(!PasswordEncryptor.checkPassword(password, targetUser.getPassword())){
      throw new CustomException(ErrorCode.PASSWORD_MATCH_ERROR);
    }
    targetUser.getStatus().updateLastOnline(Instant.now()); // 삭제 할 수도
    userRepository.save(targetUser);

    return userMapper.toDto(targetUser);
  }
}
