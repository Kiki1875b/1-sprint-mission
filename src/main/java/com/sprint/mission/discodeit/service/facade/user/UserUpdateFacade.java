package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserUpdateFacade {
  UserResponseDto updateUser(String userId, MultipartFile profile, UserUpdateDto updateDto);
}
