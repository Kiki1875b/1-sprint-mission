package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.message.MessageManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMasterFacadeImpl implements MessageMasterFacade {

  private final MessageManagementService messageManagementService;
  private final MessageMapper messageMapper;

  @Override
  public MessageResponseDto createMessage(CreateMessageDto messageDto, List<MultipartFile> files) {
    Message message = messageManagementService.createMessage(messageDto, files);
    return messageMapper.toResponseDto(message);
  }

  @Override
  public MessageResponseDto findMessageById(String id) {
    Message message = messageManagementService.findSingleMessage(id);
    return messageMapper.toResponseDto(message);
  }

  @Override
  @Transactional
  public PageResponse<MessageResponseDto> findMessagesByChannel(String channelId, Instant nextCursor, Pageable pageable) {
    Page<Message> messagePage = messageManagementService.findMessagesByChannel(channelId, nextCursor, pageable);
    List<MessageResponseDto> dtoList = messageMapper.fromEntityList(messagePage.getContent());
    Instant newCursor = dtoList.isEmpty() ? null : dtoList.get(dtoList.size() - 1).createdAt();

    return new PageResponse<>(
        dtoList,
        newCursor,
        pageable.getPageSize(),
        messagePage.hasNext(),
        messagePage.getTotalElements()
    );

  }

  @Override
  public MessageResponseDto updateMessage(String messageId, MessageUpdateDto messageDto) {
    Message message = messageManagementService.updateMessage(messageId, messageDto.newContent());
    return messageMapper.toResponseDto(message);
  }

  @Override
  public void deleteMessage(String messageId) {
    messageManagementService.deleteMessage(messageId);
  }

}
