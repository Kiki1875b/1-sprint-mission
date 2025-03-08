package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
  User saveUser(User user);
  User update(User user);
  User findUserById(String id);
  List<User> findAllUsers();
  List<User> validateAndFindAllUsersIn(List<String> userIds);
  void deleteUser(String id);
}
