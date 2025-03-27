package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.user.UserService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public User update(User user) {
    return userRepository.save(user);
  }

  @Override
  @Transactional(readOnly = true)
  public User findUserById(String id) {
    return userRepository.findById(UUID.fromString(id)).orElseThrow(
        () -> {
          log.debug("[USER NOT FOUND] [ID: {}]", id);
          return new DiscodeitException(ErrorCode.USER_NOT_FOUND);
        }
    );
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> validateAndFindAllUsersIn(List<String> userIds) {
    log.debug("[VALIDATING USERS] : [ID: {}]", userIds);
    List<UUID> userUuids = userIds.stream()
        .map(id -> {
          try {
            return UUID.fromString(id);
          } catch (IllegalArgumentException e) {
            // TODO : 예외를 던질지, 로그를 찍을지 고민
            log.debug("[USER ID NOT IN CORRECT FORMAT] : [ID: {}]", id);
            return null;
          }
        }).filter(Objects::nonNull)
        .toList();

    // TODO : 상세 exception message 작성
    if (userUuids.isEmpty()) {
      log.warn("[ATTEMPT TO CREATE PRIVATE CHANNEL WITH NO USER]");
      throw new DiscodeitException(ErrorCode.DEFAULT_ERROR_MESSAGE);
    }

    return userRepository.findAllByIdIn(userUuids);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findByAllIn(List<UUID> userIds) {
    return userRepository.findAllByIdIn(userIds);

  }

  @Override
  @Transactional
  public void deleteUser(String id) {
    userRepository.deleteById(UUID.fromString(id));
  }
}
