package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface FindMessageFacade {
  MessageResponseDto findMessageById(String id);
  PageResponse<MessageResponseDto> findMessagesByChannel(String channelId, Instant nextCursor, Pageable pageable);
}
