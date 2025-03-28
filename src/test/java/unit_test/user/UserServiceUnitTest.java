package unit_test.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

  @Mock
  private UserRepository userRepository;
  @InjectMocks
  private BasicUserService userService;
  private User user;

  @BeforeEach
  void setUp() {
    user = new User("testUser", "test@example.com", "pw", null, null);
    ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
  }

  @Test
  void findUserById_success() {
    // given
    UUID userId = user.getId();
    given(userRepository.findById(userId)).willReturn(Optional.ofNullable(user));

    // when
    User foundUser = userService.findUserById(userId.toString());

    // then
    assertThat(foundUser).isNotNull();
    assertThat(foundUser.getUsername()).isEqualTo("testUser");
    assertThat(foundUser).isEqualTo(user);
  }

  @Test
  void findUserById_fail() {
    // given
    UUID randomId = UUID.randomUUID();
    given(userRepository.findById(randomId)).willReturn(Optional.empty());

    //when
    //then
    assertThatThrownBy(() -> userService.findUserById(String.valueOf(randomId)))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void findAllUsersIn_success() {
    //given
    List<String> userIds = List.of(String.valueOf(user.getId()));
    List<User> userList = List.of(user);
    given(userRepository.findAllByIdIn(any())).willReturn(userList);

    //when
    List<User> foundUsers = userService.findAllUsersIn(userIds);

    //then
    assertThat(foundUsers).isNotEmpty();
    assertThat(foundUsers.size()).isEqualTo(1);
    assertThat(foundUsers).containsExactly(user);
  }

  @Test
  void findAllUsersIn_fail() {
    // given
    List<String> userIds = List.of();

    //when
    //then
    assertThatThrownBy(() -> userService.findAllUsersIn(userIds))
        .isInstanceOf(DiscodeitException.class)
        .hasMessageContaining(ErrorCode.DEFAULT_ERROR_MESSAGE.getMessage());
  }

  @Test
  void deleteUser_success() {
    //given
    String userId = user.getId().toString();

    // when
    userService.deleteUser(userId);

    //then
    then(userRepository).should().deleteById(UUID.fromString(userId));
  }

  @Test
  void deleteUser_successWithWrongUuid() {
    String userId = UUID.randomUUID().toString();

    userService.deleteUser(userId);

    then(userRepository).should().deleteById(UUID.fromString(userId));
  }

  @Test
  void deleteUser_invalidUuidFormat_fail() {
    String userId = "Invalid UUID";

    assertThatThrownBy(() -> userService.deleteUser(userId))
        .isInstanceOf(DiscodeitException.class);
  }
}
