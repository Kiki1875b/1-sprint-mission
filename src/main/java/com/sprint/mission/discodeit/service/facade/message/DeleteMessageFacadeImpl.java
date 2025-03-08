package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteMessageFacadeImpl implements DeleteMessageFacade{
  private final BinaryContentService binaryContentService;
  private final MessageService messageService;

  @Override //TODO : 여기 하던중
  @Transactional
  public void deleteMessage(String messageId) {
    // binaryContentService.deleteByMessageId(messageId);
    messageService.deleteMessage(messageId);
  }
}
