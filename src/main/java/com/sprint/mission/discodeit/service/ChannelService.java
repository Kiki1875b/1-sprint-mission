package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
  Channel createPrivateChannel(Channel channel);
  Channel createPublicChannel(Channel channel);
  Channel findChannelById(String channelId);
  List<Channel> findAllPublicOrChannelsIn(List<UUID> channelIds);
  Channel updateChannel(String channelId, ChannelUpdateDto dto);
  void deleteChannel(String channelId);
  void validateUserAccess(Channel channel, User user);

}
