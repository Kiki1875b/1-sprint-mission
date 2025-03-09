package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.user.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MessageManagementServiceImpl implements MessageManagementService {

  private final UserService userService;
  private final ChannelService channelService;
  private final MessageService messageService;

  private final MessageMapper messageMapper;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentService binaryContentService;


  @Override
  @Transactional
  public Message createMessage(CreateMessageDto messageDto, List<MultipartFile> files) {

    Channel channel = channelService.findChannelById(messageDto.channelId());
    User author = userService.findUserById(messageDto.authorId());

    channelService.validateUserAccess(channel, author);
    Message message = messageMapper.toEntity(messageDto);
    message.addChannel(channel);
    message.addAuthor(author);

    if (files != null && !files.isEmpty()) {
      withFiles(files, message);
    }

    return messageService.createMessage(message);
  }

  @Override
  public Message findSingleMessage(String messageId) {
    return messageService.getMessageById(messageId);
  }

  @Override
  @Transactional
  public PageResponse<MessageResponseDto> findMessagesByChannel(String channelId, Instant cursor, Pageable pageable) {

    Page<Message> channelMessages = messageService.getMessagesByChannelWithCursor(channelId, cursor, pageable);
    List<MessageResponseDto> dtoList = messageMapper.fromEntityList(channelMessages.getContent());
    Instant newCursor = dtoList.isEmpty() ? null : dtoList.get(dtoList.size() - 1).createdAt();

    return new PageResponse<>(
        dtoList,
        newCursor,
        pageable.getPageSize(),
        channelMessages.hasNext(),
        channelMessages.getTotalElements()
    );
  }

  private void withFiles(List<MultipartFile> files, Message message) {
    List<BinaryContent> contents = binaryContentMapper.fromMessageFiles(files);

    binaryContentService.saveBinaryContents(contents, files);

    List<MessageAttachment> attachments = contents.stream()
        .map(content -> new MessageAttachment(message, content))
        .toList();

    message.getAttachments().addAll(attachments);
  }


}
