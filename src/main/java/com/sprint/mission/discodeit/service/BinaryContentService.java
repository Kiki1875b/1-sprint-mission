package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BinaryContentService {
  ResponseEntity<Resource> download(String id);

  BinaryContent save(BinaryContent content, byte[] bytes);
  BinaryContent find(String id);
  List<BinaryContent> findByMessageId(String messageId);
  List<BinaryContent> findAllByIdIn(List<String> ids);
  void delete(String id);
  void deleteByMessageId(String messageId);
  List<BinaryContent> saveBinaryContents(List<BinaryContent> contents);
//  Map<String, List<BinaryContent>> getBinaryContentsFilteredByChannelAndGroupedByMessage(String channelId);
  List<BinaryContent> findAll();
}
