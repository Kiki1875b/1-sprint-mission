package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMasterFacadeImpl implements MessageMasterFacade {


  private final CreateMessageFacade createMessageFacade;
  private final FindMessageFacade findMessageFacade;
  private final UpdateMessageFacade updateMessageFacade;
  private final DeleteMessageFacade deleteMessageFacade;

  @Override
  public MessageResponseDto createMessage(CreateMessageDto messageDto, List<MultipartFile> files) {
    return createMessageFacade.createMessage(messageDto, files);
  }

  @Override
  public MessageResponseDto findMessageById(String id) {
    return findMessageFacade.findMessageById(id);
  }

  @Override
  public PageResponse<MessageResponseDto> findMessagesByChannel(String channelId, Instant nextCursor, Pageable pageable) {
    return findMessageFacade.findMessagesByChannel(channelId, nextCursor, pageable);
  }

  @Override
  public MessageResponseDto updateMessage(String messageId, MessageUpdateDto messageDto) {
    return updateMessageFacade.updateMessage(messageId, messageDto);
  }

  @Override
  public void deleteMessage(String messageId) {
    deleteMessageFacade.deleteMessage(messageId);
  }

}
