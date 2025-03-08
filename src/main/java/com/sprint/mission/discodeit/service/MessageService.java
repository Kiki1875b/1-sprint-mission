package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MessageService {
  Message createMessage(Message message);
  Message updateMessage(Message message, String content);
  Message getMessageById(String messageId);
  Page<Message> getMessagesByChannel(String channelId, Pageable pageable);

  Page<Message> getMessagesByChannelWithCursor(String channelId, Instant nextCursor, Pageable pageable);

  Message getLatestMessageByChannel(String channelId);
  Map<UUID, Instant> getLatestMessageForChannels(List<Channel> channels);
  void deleteMessage(String messageId);

}
