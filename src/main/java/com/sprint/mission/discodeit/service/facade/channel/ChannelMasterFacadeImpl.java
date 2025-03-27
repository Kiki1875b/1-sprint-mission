package com.sprint.mission.discodeit.service.facade.channel;


import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.channel.ChannelManagementService;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.message.MessageService;
import com.sprint.mission.discodeit.service.user.UserService;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelMasterFacadeImpl implements ChannelMasterFacade {

  private final ChannelManagementService channelManagementService;

  private final MessageService messageService;
  private final ChannelService channelService;
  private final ChannelMapper channelMapper;
  private final UserService userService;

  private final ReadStatusService readStatusService;

  @Override
  @Transactional
  public ChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto) {

    Channel channel = channelManagementService.createPrivateChannel(
        channelMapper.toEntity(channelDto),
        channelDto.participantIds()
    );

    log.debug("[CREATED PRIVATE CHANNEL AND READ STATUSES] : [USER_IDS : {}]",
        channelDto.participantIds());

    List<User> participants = channel.getStatuses().stream()
        .map(ReadStatus::getUser).toList();

    return channelMapper.toDto(channel, Instant.EPOCH, participants);
  }

  @Override
  @Transactional
  public ChannelResponseDto createPublicChannel(CreateChannelDto channelDto) {
    Channel channel = channelManagementService.createPublicChannel(
        channelMapper.toEntity(channelDto));
    log.debug("[CREATED PUBLIC CHANNEL] : [ID: {}][NAME: {}]", channel.getId(), channel.getName());
    return channelMapper.toDto(channel, Instant.EPOCH, Collections.emptyList());
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelResponseDto getChannelById(String channelId) {
    Channel channel = channelService.findChannelById(channelId);

    List<User> participants = channel.getStatuses().stream()
        .map(ReadStatus::getUser)
        .toList();

    Instant lastMessageTime = channelManagementService.getLastMessageTimeForChannel(channelId);

    return channelMapper.toDto(channel, lastMessageTime, participants);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelResponseDto> findAllChannelsByUserId(String userId) {

    List<Channel> channels = channelService.findAllChannelsByUserId(userId);
    Map<UUID, Instant> latestMessageTimeByChannel = messageService.getLatestMessageForChannels(
        channels);
    Map<UUID, List<User>> channelParticipants = channelMapper.channelToParticipants(channels);

    return channelMapper.toListResponse(channels, latestMessageTimeByChannel, channelParticipants);
  }

  @Override
  // userId 가 주어졌을때, 해당 user 가 참여중인 체널은 readstatus에 정보가 있다. 하지만, 해당 user가 참여중이지 않은 채널은 첫 쿼리에서 불러오지 못한다 -> 불러오려면 join
  @Transactional(readOnly = true)
  public List<ChannelResponseDto> findAllChannelsByUserIdV2(String userId) {

    // 쿼리 2번 -> userId 로 user 의 read status + 불러온 read status 에 포함된 모든 channel 의 다른 user 의 read status
    List<ReadStatus> statuses = readStatusService.findAllByUserId(userId);
    List<UUID> channelIds = statuses.stream().map(status -> status.getChannel().getId()).distinct()
        .collect(Collectors.toList());
    // 쿼리 1번 -> 불러온 read_status 들의 channel 로 channel 쿼리
    List<Channel> channels = channelService.findAllChannelsInOrPublic(channelIds);

    // 불러온 channelId -> channelIds 랑 다른점 : public 포함
    List<UUID> fetchedChannelIds = channels.stream().map(c -> c.getId())
        .collect(Collectors.toList());
    // 기존 channelIds 와 fetchedChannelIds 의 다른 id 목록
    List<UUID> difference = fetchedChannelIds.stream()
        .filter(id -> !channelIds.contains(id))
        .collect(Collectors.toList());

    // 쿼리 1번 -> 기존에 없던 channelIds 에 대한 read_status 쿼리
    List<ReadStatus> newReadStatus = readStatusService.findAllInChannel(difference);

    List<UUID> newChannelIds = newReadStatus.stream().map(status -> status.getChannel().getId())
        .distinct().collect(Collectors.toList());
    // 쿼리 1번 -> 기존에 없던 channel 쿼리
    List<Channel> newChannels = channelService.findAllChannelsInOrPublic(newChannelIds);

    // 체널 목록 중복 제거
    List<Channel> mergedChannels = Stream.concat(channels.stream(), newChannels.stream())
        .distinct()
        .collect(Collectors.toList());

    List<ReadStatus> mergedStatuses = Stream.concat(newReadStatus.stream(), statuses.stream())
        .distinct()
        .collect(Collectors.toList());

    List<String> userIds = mergedStatuses.stream()
        .map(status -> status.getUser().getId().toString()).collect(Collectors.toList());

    List<User> users = userService.validateAndFindAllUsersIn(userIds);

    Map<UUID, Instant> latestMessageTimeByChannel = messageService.getLatestMessageForChannels(
        mergedChannels);

    Map<UUID, List<User>> channelParticipants = channelMapper.channelToParticipants(mergedChannels);

    return channelMapper.toListResponse(mergedChannels, latestMessageTimeByChannel,
        channelParticipants);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelResponseDto> findAllChannelsByUserIdV3(String userId) {
    List<Channel> channels = channelManagementService.findAllChannelsForUser(userId);

    Map<UUID, Instant> latestMessageTimeByChannel = messageService.getLatestMessageForChannels(
        channels);
    Map<UUID, List<User>> channelParticipants = channelMapper.channelToParticipants(channels);
    return channelMapper.toListResponse(channels, latestMessageTimeByChannel, channelParticipants);
  }

  @Override
  @Transactional
  public ChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto) {

    Channel channel = channelService.updateChannel(channelId, channelUpdateDto);
    log.debug("[UPDATED CHANNEL] : [ID: {}]", channelId);

    List<ReadStatus> statuses = readStatusService.findAllByChannelId(channelId);
    log.debug("[FOUND READ STATUSES] : [CHANNEL_ID: {}]", channelId);

    List<UUID> userIds = statuses.stream().map(status -> status.getUser().getId())
        .collect(Collectors.toList());

    List<User> users = userService.findByAllIn(userIds);
    log.debug("[FOUND USERS] : [USER_IDS : {}]", userIds);

    Instant lastMessageTime = channelManagementService
        .getLastMessageTimeForChannel(channelId);

    log.debug("[FOUND LATEST MESSAGE TIME] : [TIME : {}]", lastMessageTime);
    return channelMapper.toDto(channel, lastMessageTime, users);
  }

  @Override
  @Transactional
  public void deleteChannel(String channelId) {
    if (channelId.isBlank()) {
      throw new DiscodeitException(ErrorCode.INVALID_UUID_FORMAT, Map.of("channelId", channelId));
    }
    channelService.deleteChannel(channelId);
  }

}
