package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class
BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;

  @Override
  public Message createMessage(Message message) {
    return messageRepository.save(message);
  }

  @Override
  public Message updateMessage(Message message, String content) {
    if (content != null && !content.isEmpty()) {
      message.addContent(content);
    }
    return messageRepository.save(message);
  }

  @Override
  public Message getMessageById(String messageId) {
    return messageRepository.findById(UUID.fromString(messageId))
        .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));
  }

  @Override
  public Page<Message> getMessagesByChannel(String channelId, Pageable pageable) {
    return messageRepository.findByChannel_Id(UUID.fromString(channelId), pageable);
  }

  @Override
  public Page<Message> getMessagesByChannelWithCursor(String channelId, Instant nextCursor, Pageable pageable)  {
    if(nextCursor == null){
      nextCursor = Instant.now();
    }
    return messageRepository.findByChannel_IdAndCreatedAtLessThan(UUID.fromString(channelId), nextCursor, pageable);
  }

  @Override
  public Message getLatestMessageByChannel(String channelId) {
    return messageRepository.findTopByChannel_IdOrderByCreatedAtDesc(UUID.fromString(channelId))
        .orElse(null);
  }

  @Override
  public Map<UUID, Instant> getLatestMessageForChannels(List<Channel> channels) {
    List<UUID> channelIds = channels.stream().map(Channel::getId).toList();

    List<Message> latestMessages = messageRepository.findLatestMessagesForEachChannel(channelIds);

    return latestMessages.stream()
        .collect(Collectors.toMap(
            message -> message.getChannel().getId(),
            Message::getCreatedAt
        ));
  }

  @Override
  public void deleteMessage(String messageId) {
    messageRepository.deleteById(UUID.fromString(messageId));
  }
}
