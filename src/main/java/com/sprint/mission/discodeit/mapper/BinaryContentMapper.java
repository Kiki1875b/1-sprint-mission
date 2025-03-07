package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
    ,imports = BinaryContentUtil.class)
public interface BinaryContentMapper {

  @Mapping(source = "file.originalFilename", target = "fileName")
  @Mapping(source = "file.contentType", target = "contentType")
  @Mapping(source = "file.size", target = "size")
  BinaryContent toMessageBinaryContent(MultipartFile file);

  @Mapping(source = "file.originalFilename", target = "fileName")
  @Mapping(source = "file.contentType", target = "contentType")
  @Mapping(source = "file.size", target = "size")
  BinaryContent toProfileBinaryContent(MultipartFile file);

  @Mapping(target = "id", source = "attachment.id")
  @Mapping(target = "fileName", source = "attachment.fileName")
  @Mapping(target = "size", source = "attachment.size")
  @Mapping(target = "contentType", source = "attachment.contentType")
  BinaryContentDto toBinaryContentDto(MessageAttachment attachment);

  default BinaryContent safeToMessageBinaryContent(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return null; // 파일이 null이거나 크기가 0이면 null 반환
    }
    return toMessageBinaryContent(file);
  }


  default List<BinaryContent> fromMessageFiles(List<MultipartFile> files, UUID userId, UUID channelId, UUID messageId) {
    return files == null || files.isEmpty() ? Collections.emptyList()
        : files.stream()
        .map(file -> toMessageBinaryContent(file))
        .collect(Collectors.toList());
  }

  @Mapping(target = "id", source = "id")
  @Mapping(target = "size", source = "size")
  @Mapping(target = "contentType", source = "contentType")
  BinaryContentDto toDto(BinaryContent content);

  List<BinaryContentDto> toDtoList(List<BinaryContent> contents);
}
