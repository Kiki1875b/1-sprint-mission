package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusResponseDto;


public interface UserStatusService {

  UserStatusResponseDto updateByUserId(String userId, UpdateUserStatusDto dto);

}
