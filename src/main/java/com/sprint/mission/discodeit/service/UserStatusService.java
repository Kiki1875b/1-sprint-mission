package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusResponseDto;


public interface UserStatusService {
  // UserStatus create(UserStatus dto);
  // UserStatus find(String id);
  //UserStatus findByUserId(String userId);
  // List<UserStatus> findAll();
  UserStatusResponseDto updateByUserId(String userId, UpdateUserStatusDto dto);

}
