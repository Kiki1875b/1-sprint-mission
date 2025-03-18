package com.sprint.mission.discodeit.storage;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {
  UUID put(UUID id, byte[] bytes);
  InputStream get(UUID id);
  ResponseEntity<?> download(UUID id) throws IOException;
}
