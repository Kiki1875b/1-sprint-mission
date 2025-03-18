package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
@ConditionalOnProperty(value = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  @Value(value = "${discodeit.storage.local.root-path}")
  Path root;

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_ERROR);
    }
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path filePath = resolvePath(id);
    try {
      Files.write(filePath, bytes);
      return id;
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_ERROR);
    }
  }

  @Override
  public InputStream get(UUID id) {
    Path filePath = resolvePath(id);
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_ERROR);
    }
  }


  @Override
  public ResponseEntity<Resource> download(UUID id) {

    Path filePath = resolvePath(id);

    if (!Files.exists(filePath)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    try {
      InputStream inputStream = get(id);
      String contentType = Files.probeContentType(filePath);
      if (contentType == null) {
        contentType = "application/octet-stream";
      }

      long fileSize = Files.size(filePath);
      String fileName = filePath.getFileName().toString();
      Resource resource = new InputStreamResource(inputStream);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
          .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize))
          .contentType(MediaType.parseMediaType(contentType))
          .body(resource);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }


  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}
