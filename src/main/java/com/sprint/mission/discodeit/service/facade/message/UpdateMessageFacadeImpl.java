package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateMessageFacadeImpl implements UpdateMessageFacade{

  private final MessageService messageService;
  private final MessageMapper messageMapper;

  @Override
  public MessageResponseDto updateMessage(String messageId, MessageUpdateDto messageDto) {

    Message message = messageService.getMessageById(messageId);

    messageService.updateMessage(message, messageDto.newContent());

    return messageMapper.toResponseDto(message);
  }
}
