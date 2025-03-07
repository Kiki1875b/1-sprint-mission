package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;


  @Override
  public Channel createPrivateChannel(Channel channel) {
    return channelRepository.save(channel);
  }


  @Override
  public Channel createPublicChannel(Channel channel) {
    return channelRepository.save(channel);
  }

  @Override
  public void validateUserAccess(Channel channel, User user) {
    if (Objects.equals(channel.getType(), Channel.ChannelType.PRIVATE)) {
      Optional<ReadStatus> status = readStatusRepository.findByUserAndChannel(user, channel);
      if (status.isEmpty()) {
        throw new CustomException(ErrorCode.NO_ACCESS_TO_CHANNEL);
      }
    }
  }

  @Override
  public Channel findChannelById(String channelId) {
    return channelRepository.findById(UUID.fromString(channelId))
        .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

  }

  @Override
  public List<Channel> findAllPublicOrChannelsIn(List<UUID> channelIds){
    // TODO : 한번에 가져오는 쿼리
    List<Channel> privateVisible = channelRepository.findAllById(channelIds);
    List<Channel> publicChannel = channelRepository.findAllByType(Channel.ChannelType.PUBLIC);


    return List.of(privateVisible, publicChannel).stream()
        .flatMap(List::stream).collect(Collectors.toList());
  }


  @Override
  public Channel updateChannel(String channelId, ChannelUpdateDto dto) {

    Channel channel = channelRepository.findById(UUID.fromString(channelId)).orElseThrow(
      () -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND)
    );

    if (Objects.equals(channel.getType(), Channel.ChannelType.PRIVATE)) {
      throw new CustomException(ErrorCode.PRIVATE_CHANNEL_CANNOT_BE_UPDATED);
    }

    channel.updateChannelName(dto.newName());
    channel.updateDescription(dto.newDescription());

    return channelRepository.save(channel);
  }

  @Override
  public void deleteChannel(String channelId) {
    channelRepository.deleteById(UUID.fromString(channelId));
  }

}
