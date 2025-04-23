package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
    uses = {BinaryContentMapper.class, UserMapper.class}
)
public interface MessageMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "attachments", expression = "java(new ArrayList<>())")
  Message toEntity(CreateMessageDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "content", source = "content")
  @Mapping(target = "attachments", expression = "java(new ArrayList<>())")
  Message toEntity(String content);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "createdAt", source = "createdAt")
  @Mapping(target = "updatedAt", source = "updatedAt")
  @Mapping(target = "content", source = "content")
  @Mapping(target = "channelId", source = "channel.id")
  @Mapping(target = "author", source = "author")
  @Mapping(target = "attachments", source = "attachments")
  MessageResponseDto toResponseDto(Message message);

  List<MessageResponseDto> fromEntityList(List<Message> messages);
//  @Named("convertToBase64")
//  default List<String> convertToBase64(List<BinaryContent> binaryContents) {
//    return binaryContents == null || binaryContents.isEmpty()
//        ? null
//        : binaryContents.stream()
//        .map(content -> Base64.getEncoder().encodeToString(content.getData()))
//        .toList();
//  }
}


