package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final MessageAttachmentRepository messageAttachmentRepository;

  @Override
  public BinaryContent save(BinaryContent content) {
    return binaryContentRepository.save(content);
  }

  @Override
  public BinaryContent find(String id) {
    return binaryContentRepository.findById(UUID.fromString(id)).orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND));
  }

  @Override
  public List<BinaryContent> findByMessageId(String messageId) {
    List<MessageAttachment> attachments = messageAttachmentRepository.findByMessageIdWithAttachments(UUID.fromString(messageId));
    List<BinaryContent> contents = attachments.stream()
        .map(attachment -> attachment.getAttachment()).collect(Collectors.toList());

    return (contents == null || contents.isEmpty())
        ? Collections.emptyList()
        : Collections.unmodifiableList(contents);
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<String> ids) {
    List<UUID> uuids = ids.stream().map(UUID::fromString).toList();
    return Collections.unmodifiableList(binaryContentRepository.findAllById(uuids));
  }

  @Override
  public void delete(String id) {
    binaryContentRepository.deleteById(UUID.fromString(id));
  }

  @Override
  public void deleteByMessageId(String messageId) {
    List<UUID> contents = messageAttachmentRepository.findByMessageId(UUID.fromString(messageId));
    binaryContentRepository.deleteAllById(contents);
  }

  @Override
  public List<BinaryContent> saveBinaryContents(List<BinaryContent> contents) {
    if (contents == null || contents.isEmpty()) {
      return Collections.emptyList();
    }
    return binaryContentRepository.saveAll(contents);
  }

//  @Override
//  public Map<String, List<BinaryContent>> getBinaryContentsFilteredByChannelAndGroupedByMessage(String channelId) {
//    return binaryContentRepository.findByChannel(channelId).stream()
//        .collect(Collectors.groupingBy( content ->
//            content.getMessageId() != null ? content.getMessageId() : "",
//            Collectors.toList()
//        ));
//  }

  @Override
  public List<BinaryContent> findAll(){
    return binaryContentRepository.findAll();
  }
}
