package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  List<Channel> findAllByType(Channel.ChannelType type);

  List<Channel> findByIdInOrType(List<UUID> channelIds, Channel.ChannelType type);

  @EntityGraph(attributePaths = {"statuses", "statuses.user", "statuses.user.status", "statuses.user.profile"})
  @Query("""
          SELECT c 
          FROM Channel c
          WHERE c IN (
              SELECT rs.channel FROM ReadStatus rs WHERE rs.user.id = :userId
          ) OR c.type = 'PUBLIC'
      """)
  List<Channel> findPrivateChannels(@Param("userId") UUID userId);
}
