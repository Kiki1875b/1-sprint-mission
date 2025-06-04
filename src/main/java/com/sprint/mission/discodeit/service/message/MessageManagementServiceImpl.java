package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.async.BinaryContentStorageWrapperService;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.UploadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.user.UserService;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageManagementServiceImpl implements MessageManagementService {

  private final UserService userService;
  private final ChannelService channelService;
  private final MessageService messageService;

  private final MessageMapper messageMapper;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentService binaryContentService;
  private final BinaryContentStorageWrapperService binaryContentStorageWrapperService;

  @Override
  @Transactional
  public Message createMessage(String content, String channelId, String authorId,
      List<MultipartFile> files) {

    Channel channel = channelService.findChannelById(channelId);
    log.debug("[FOUND CHANNEL] : [ID : {}]", channelId);
    User author = userService.findUserById(authorId);
    log.debug("[FOUND AUTHOR] : [ID : {}]", authorId);
    channelService.validateUserAccess(channel, author);
    log.debug("[USER VALIDATED FOR CHANNEL] : [CHANNEL_ID: {}] , [AUTHOR_ID : {}]", channelId,
        authorId);

    Message message = messageMapper.toEntity(content);
    message.addChannel(channel);
    message.addAuthor(author);

    if (files != null && !files.isEmpty()) {
      log.debug("[ATTACHMENTS FOUND] : [CHANNEL_ID : {}]", channelId);
      withFiles(files, message);
    }

    return messageService.createMessage(message);
  }

  @Override
  public Message findSingleMessage(String messageId) {
    return messageService.getMessageById(messageId);
  }

  @Override
  public Page<Message> findMessagesByChannel(String channelId, Instant cursor, Pageable pageable) {
    Page<Message> channelMessages =
        messageService.getMessagesByChannelWithCursor(channelId, cursor,
            pageable);

    List<UUID> authorIds = channelMessages.getContent().stream()
        .map(message -> message.getAuthor().getId())
        .distinct()
        .collect(Collectors.toList());

    List<User> users = userService.findByAllIn(authorIds);

    return channelMessages;
  }

  @Override
  @Transactional
  public Message updateMessage(String messageId, String newContent) {
    Message message = messageService.getMessageById(messageId);
    messageService.updateMessage(message, newContent);

    log.debug("[UPDATED MESSAGE] : [ID : {}]", messageId);

    return message;
  }

  @Override
  @Transactional
  public void deleteMessage(String messageId) {
    messageService.deleteMessage(messageId);
  }

  private void withFiles(List<MultipartFile> files, Message message) {
    List<BinaryContent> contents = binaryContentMapper.fromMessageFiles(files);
    contents.forEach(content -> content.changeUploadStatus(UploadStatus.WAITING));

    List<BinaryContent> savedContents = binaryContentService.saveBinaryContents(contents, files);

    for (int i = 0; i < savedContents.size(); i++) {
      BinaryContent content = savedContents.get(i);
      MultipartFile file = files.get(i);
      content.changeUploadStatus(UploadStatus.WAITING);
      CompletableFuture<Boolean> future = null;

      try {
        future = binaryContentStorageWrapperService.uploadFile(content.getId(), file.getBytes());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      future.whenComplete((success, ex) -> {
        if (ex != null) {
          content.changeUploadStatus(UploadStatus.FAILED);
          binaryContentService.update(content);
          log.warn("[ATTACHMENT UPLOAD FAILED - EXCEPTION] : [CONTENT_ID: {}]", content.getId());
        } else if (success) {
          content.changeUploadStatus(UploadStatus.SUCCESS);
          binaryContentService.update(content);
          log.info("[ATTACHMENT UPLOAD SUCCESS] : [CONTENT_ID: {}]", content.getId());
        } else {
          content.changeUploadStatus(UploadStatus.FAILED);
          binaryContentService.update(content);
          log.warn("[ATTACHMENT UPLOAD FAILED - RECOVER] : [CONTENT_ID: {}]", content.getId());
        }
      });
    }

    List<MessageAttachment> attachments = contents.stream()
        .map(content -> new MessageAttachment(message, content))
        .toList();

    message.getAttachments().addAll(attachments);

    log.debug("[ATTACHMENTS SAVED]");

  }
}
