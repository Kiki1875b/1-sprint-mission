package unit_test.channel;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import unit_test.TestEntityFactory;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceUnitTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @InjectMocks
  private BasicChannelService basicChannelService;

  private User user;
  private Channel publicChannel;
  private Channel privateChannel;

  @BeforeEach
  void setUp() {
    user = TestEntityFactory.createUser("test", "t@gmail.com");
    publicChannel = TestEntityFactory.createPublicChannel();
    privateChannel = TestEntityFactory.createPrivateChannel();
  }

  @Test
  void validateUserAccess_shouldDoNothing_whenPublicChannel() {
    //when
    basicChannelService.validateUserAccess(publicChannel, user);

    //then
    then(readStatusRepository).shouldHaveNoInteractions();
  }

  @Test
  void validateUserAccess_shouldThrowException_emptyStatus() {
    //given
    given(readStatusRepository.findByUserAndChannel(user, privateChannel))
        .willReturn(Optional.empty());

    //when & then
    Assertions.assertThatThrownBy(
            () -> basicChannelService.validateUserAccess(privateChannel, user))
        .isInstanceOf(ChannelException.class)
        .hasMessageContaining(ErrorCode.NO_ACCESS_TO_CHANNEL.getMessage());

  }
}
