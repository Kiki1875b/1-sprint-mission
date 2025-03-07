package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public interface MessageAttachmentMapper {

  @Mapping(target = "id", source = "attachment.id")
  @Mapping(target = "fileName", source = "attachment.fileName")
  @Mapping(target = "size", source = "attachment.size")
  @Mapping(target = "contentType", source = "attachment.contentType")
  @Mapping(target = "bytes", source = "attachment.bytes")
  BinaryContentDto toBinaryContentDto(MessageAttachment attachment);
}
