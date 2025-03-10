package tmp;


import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;



@SpringBootTest(classes = DiscodeitApplication.class)
public class QueryTest {

  private final ChannelRepository channelRepository;

  @Autowired
  public QueryTest(ChannelRepository channelRepository) {
    this.channelRepository = channelRepository;
  }


  @Test
  @Transactional
  void test(){
    Channel channel = channelRepository.findById(
        UUID.fromString("3787cf1f-c333-47ea-9820-9209b446eec1")
    ).orElseThrow();

    for(ReadStatus status: channel.getStatuses()){
      System.out.println(status.getUser().getId());
    }
  }
}
