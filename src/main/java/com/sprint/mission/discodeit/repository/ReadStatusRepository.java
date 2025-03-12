package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  Optional<ReadStatus> findByUserAndChannel(User user, Channel channel);

  List<ReadStatus> findAllByChannel_Id(UUID channelId);

  List<ReadStatus> findAllByUser_Id(UUID userId);
  List<ReadStatus> findAllByChannel_IdIn(List<UUID> channelId);

  @Query("""
    SELECT rs FROM ReadStatus rs JOIN FETCH rs.user WHERE rs.channel.id = :channelId
      """)
  List<ReadStatus> findAllByChannelIdWithUsers(@Param("channelId") UUID channelId);

}
