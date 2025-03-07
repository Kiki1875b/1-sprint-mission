package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

@Component
@ConditionalOnProperty(value = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage{

  @Value(value = "${discodeit.storage.local.root-path}")
  Path root;

  @Override
  public UUID put(UUID id, byte[] bytes) {
    return null;
  }

  @Override
  public InputStream get(UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto dto) {
    return null;
  }
}
