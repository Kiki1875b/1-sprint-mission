package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.MessageAttachmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageAttachmentRepository extends JpaRepository<MessageAttachment, MessageAttachmentId> {
  @Query("""
    SELECT ma FROM MessageAttachment ma 
    JOIN FETCH ma.attachment 
    WHERE ma.message.id = :messageId
""")
  List<MessageAttachment> findByMessageIdWithAttachments(UUID messageId);


  @Query("SELECT ma.attachment.id FROM MessageAttachment ma JOIN ma.attachment WHERE ma.message.id = :messageId")
  List<UUID> findByMessageId(UUID messageId);

  List<MessageAttachment> findAllByMessage_IdIn(List<UUID> messageIds);
}
