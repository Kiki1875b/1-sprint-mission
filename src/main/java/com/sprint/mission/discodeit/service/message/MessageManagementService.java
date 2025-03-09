package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

public interface MessageManagementService {

  Message createMessage(CreateMessageDto messageDto, List<MultipartFile> files);

  Message findSingleMessage(String messageId);

  PageResponse<MessageResponseDto> findMessagesByChannel(String channelId, Instant cursor, Pageable pageable);
}
