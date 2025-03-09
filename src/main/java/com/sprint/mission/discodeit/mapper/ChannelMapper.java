package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = UserMapper.class, imports = Objects.class)
public interface ChannelMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", source = "name")
  @Mapping(target = "type", constant = "PUBLIC")
  Channel toEntity(CreateChannelDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", constant = "PRIVATE")
  Channel toEntity(CreatePrivateChannelDto dto);

  @Mapping(source = "channel.id", target = "id")
  @Mapping(source = "channel.type", target = "type")
  @Mapping(target = "name", source = "channel.name", defaultExpression = "java(\"\")")
  @Mapping(target = "description", source = "channel.description", defaultExpression = "java(\"\")")
  @Mapping(target = "lastMessageAt", source = "lastMessageAt", defaultExpression = "java(Instant.EPOCH)")
  @Mapping(target = "participants", source = "participants", defaultExpression = "java(new ArrayList<>())")
  ChannelResponseDto toDto(Channel channel, Instant lastMessageAt, List<User> participants);

}


