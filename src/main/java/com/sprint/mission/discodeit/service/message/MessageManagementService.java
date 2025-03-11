package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

public interface MessageManagementService {

  Message createMessage(String content, String channelId, String authorId, List<MultipartFile> files);

  Message findSingleMessage(String messageId);

  Page<Message> findMessagesByChannel(String channelId, Instant cursor, Pageable pageable);

  Message updateMessage(String messageId, String newContent);
  void deleteMessage(String messageId);
}
