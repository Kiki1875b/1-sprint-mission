package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
  ReadStatus create(CreateReadStatusDto dto);
  List<ReadStatus> createMultipleReadStatus(List<User> users, Channel channel);
  ReadStatus find(String id);

  List<ReadStatus> findByIds(List<UUID> uuids);

  List<ReadStatus> findAllByUserId(String userId);

  List<ReadStatus> findAllInChannel(List<UUID> channelIds);

  List<UUID> findAllChannelIdsByUserId(String userId);

  List<ReadStatus> findAllReadStatusRelatedToUserId(String userId);

  List<UUID> findParticipantsByChannelId(String channelId);

  List<ReadStatus> findAllByChannelId(String channelId);

  ReadStatus findByUserAndChannel(User user, Channel channel);
  ReadStatus updateById(UpdateReadStatusDto readStatusDto, String id);

}
