package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
  List<Message> findByChannel_Id(UUID channelId);
  Optional<Message> findTopByChannel_IdOrderByCreatedAtDesc(UUID channelId);


  @Query("""
        SELECT m FROM Message m 
        WHERE m.channel.id IN :channelIds
        AND m.createdAt = 
          (
            SELECT MAX(m2.createdAt) FROM Message m2 
            WHERE m2.channel.id = m.channel.id
          )
      """)
  List<Message> findLatestMessagesForEachChannel(@Param("channelIds") List<UUID> channelIds);
}
