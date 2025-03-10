package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
  List<Channel> findAllByType(Channel.ChannelType type);

  @Query("""
        SELECT DISTINCT c FROM Channel c
        JOIN FETCH c.statuses rs
        JOIN FETCH rs.user
        WHERE rs.user.id = :userId OR rs.channel.id = c.id  
    """)
  List<Channel> findPrivateChannels(@Param("userId") UUID userId);

  @Query("""
    SELECT c FROM Channel c 
    WHERE c.type = 'PUBLIC'
      """)
  List<Channel> findAllPublicChannels();
}
