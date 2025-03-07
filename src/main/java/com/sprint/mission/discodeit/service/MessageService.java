package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MessageService {
  Message createMessage(Message message);
  Message updateMessage(Message message, String content);
  Message getMessageById(String messageId);
  List<Message> getMessagesByChannel(String channelId);
  Message getLatestMessageByChannel(String channelId);
  Map<UUID, Instant> getLatestMessageForChannels(List<Channel> channels);
  void deleteMessage(String messageId);

}
