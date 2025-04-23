package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
  ReadStatus create(CreateReadStatusDto dto);
  ReadStatus find(String id);

  List<ReadStatus> findAllByUserId(String userId);

  List<ReadStatus> findAllInChannel(List<UUID> channelIds);

  List<ReadStatus> findAllByChannelId(String channelId);

  ReadStatus updateById(UpdateReadStatusDto readStatusDto, String id);

}
