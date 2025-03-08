package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserManagementService {

  User createUser(User user, MultipartFile profile);

  User updateUser(String userId, User user, MultipartFile profile);

}
