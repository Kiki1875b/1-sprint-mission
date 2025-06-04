package com.sprint.mission.discodeit.async.binary_content;


import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinaryContentStorageWrapperService {

  private final BinaryContentStorageAsyncService asyncService;

//  @Autowired
//  @Qualifier("defaultExecutor")
//  private ThreadPoolTaskExecutor defaultExecutor;

  @Async("defaultExecutor")
  public CompletableFuture<Boolean> uploadFile(UUID id, byte[] bytes) {
    try {
      boolean success = asyncService.uploadFile(id, bytes);
      return CompletableFuture.completedFuture(success);
    } catch (Exception e) {
      return CompletableFuture.failedFuture(e);
    }
  }
}
