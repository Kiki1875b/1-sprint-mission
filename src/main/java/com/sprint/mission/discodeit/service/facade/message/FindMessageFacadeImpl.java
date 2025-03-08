package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindMessageFacadeImpl implements FindMessageFacade{

  private final MessageService messageService;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageMapper;
  private final MessageAttachmentRepository messageAttachmentRepository;

  @Override
  @Transactional
  public MessageResponseDto findMessageById(String id) {
    Message message = messageService.getMessageById(id);
    return messageMapper.toResponseDto(message);
  }

  @Override
  @Transactional
  public PageResponse<MessageResponseDto> findMessagesByChannel(String channelId, Instant nextCursor, Pageable pageable) {

    Page<Message> channelMessages = messageService.getMessagesByChannelWithCursor(channelId, nextCursor, pageable);

    List<MessageResponseDto> dtoList = channelMessages.stream()
        .map(messageMapper::toResponseDto)
        .toList();

    Page<MessageResponseDto> dtoPage = new PageImpl<>(dtoList, pageable, channelMessages.getTotalElements());

    Instant newCursor = (dtoList != null && !dtoList.isEmpty()) ? dtoList.get(dtoList.size() - 1).createdAt() : Instant.EPOCH;

    return pageMapper.fromPage(dtoPage, newCursor);
  }
}
