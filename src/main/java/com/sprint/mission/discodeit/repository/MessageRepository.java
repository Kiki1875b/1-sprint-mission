package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Page<Message>  findByChannel_Id(UUID channelId, Pageable pageable);
  Optional<Message> findTopByChannel_IdOrderByCreatedAtDesc(UUID channelId);
  Page<Message> findByChannel_IdAndCreatedAtLessThan(UUID channelId, Instant createdAt, Pageable pageable);
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
