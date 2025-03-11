package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;

public interface ChannelManagementService {
  Channel createPrivateChannel(Channel channel, List<String> userIds);

  Channel createPublicChannel(Channel channel);

  Instant getLastMessageTimeForChannel(String channelId);

  List<User> getChannelParticipants(String channelId);

  List<Channel> findAllChannelsForUser(String userId);
}
