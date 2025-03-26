package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserResponseDto login(String username, String password) {

    User targetUser = userRepository.findByUsernameWithProfileAndStatus(username)
        .orElseThrow(() -> {
              log.info("[LOGIN FAILED] : [USER WITH USERNAME {} NOT FOUND]", username);
              return new CustomException(ErrorCode.USER_NOT_FOUND);
            }
        );

    if (!PasswordEncryptor.checkPassword(password, targetUser.getPassword())) {
      log.info("[LOGIN FAILED] : [PASSWORD DOES NOT MATCH FOR USER: {}]", username);
      throw new CustomException(ErrorCode.PASSWORD_MATCH_ERROR);
    }

    targetUser.getStatus().updateLastOnline(Instant.now());
    userRepository.save(targetUser);

    log.info("[LOGIN SUCCESS] [USERNAME : {}]", username);
    return userMapper.toDto(targetUser);

  }
}
