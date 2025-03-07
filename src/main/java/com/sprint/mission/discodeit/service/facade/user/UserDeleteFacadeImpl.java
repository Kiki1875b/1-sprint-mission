package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDeleteFacadeImpl implements UserDeleteFacade {

  private final UserService userService;
  private final BinaryContentService binaryContentService;

  @Override
  public void delete(String userId) {
    User user = userService.findUserById(userId);

    userService.deleteUser(userId);
    if(user.getProfile() != null){
      binaryContentService.delete(String.valueOf(user.getProfile().getId()));
    }
  }
}
