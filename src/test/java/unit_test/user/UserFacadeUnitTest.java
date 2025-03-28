package unit_test.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.facade.user.UserFacadeImpl;
import com.sprint.mission.discodeit.service.user.UserManagementService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class UserFacadeUnitTest {

  @Mock
  private UserManagementService userManagementService;
  @Mock
  private UserMapper userMapper;
  @InjectMocks
  private UserFacadeImpl userFacade;


  @Test
  void createUser_shouldCallMapperAndServiceAndReturnResponse() {
    // given
    CreateUserRequest request = new CreateUserRequest("testUsername", "pwd", "test@example.com");
    MockMultipartFile mockProfile = new MockMultipartFile("profile", "test.jpg", "image/jpeg",
        "test".getBytes());
    User user = mock(User.class);
    User createdUser = mock(User.class);
    UserResponseDto response = mock(UserResponseDto.class);

    given(userMapper.toEntity(request)).willReturn(user);
    given(userManagementService.createUser(user, mockProfile)).willReturn(createdUser);
    given(userMapper.toDto(createdUser)).willReturn(response);

    // when
    UserResponseDto result = userFacade.createUser(request, mockProfile);

    //then
    assertThat(result).isEqualTo(response);
    then(userMapper).should().toEntity(request);
    then(userManagementService).should().createUser(user, mockProfile);
    then(userMapper).should().toDto(createdUser);
  }

  @Test
  void updateUser_shouldCallMapperAndServiceAndReturnResponse() {
    String userId = UUID.randomUUID().toString();
    MockMultipartFile mockProfile = new MockMultipartFile("profile", "test.jpg", "image/jpeg",
        "test".getBytes());
    UserUpdateDto req = new UserUpdateDto("new", "new@gmail.com", "newPwd");
    User tmpUser = mock(User.class);
    User updatedUser = mock(User.class);
    UserResponseDto res = mock(UserResponseDto.class);

    given(userMapper.toEntity(req)).willReturn(tmpUser);
    given(userManagementService.updateUser(userId, tmpUser, mockProfile)).willReturn(updatedUser);
    given(userMapper.toDto(updatedUser)).willReturn(res);

    // when
    UserResponseDto response = userFacade.updateUser(userId, mockProfile, req);

    //then
    assertThat(response).isEqualTo(res);
    then(userMapper).should().toEntity(req);
    then(userManagementService).should().updateUser(userId, tmpUser, mockProfile);
    then(userMapper).should().toDto(updatedUser);
  }

  @Test
  void deleteUser_shouldCallService() {
    // given
    String id = UUID.randomUUID().toString();

    //when
    userFacade.deleteUser(id);

    //then
    then(userManagementService).should().deleteUser(id);
  }
}
