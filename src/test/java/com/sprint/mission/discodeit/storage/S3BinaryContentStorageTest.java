package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.storage.s3.S3BinaryContentStorage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Testcontainers
public class S3BinaryContentStorageTest {

  private static final String BUCKET = "test-bucket";
  private static final Region REGION = Region.AP_NORTHEAST_2;
  private static final int EXPIRATION = 60;
  @Container
  static LocalStackContainer localstack = new LocalStackContainer(
      DockerImageName.parse("localstack/localstack")
  ).withServices(LocalStackContainer.Service.S3);

  private static S3BinaryContentStorage storage;
  private static UUID id = UUID.randomUUID();
  private static byte[] content = "test content".getBytes();

  @BeforeAll
  static void init() throws Exception {
    String accessKey = localstack.getAccessKey();
    String secretKey = localstack.getSecretKey();
    URI endpoint = localstack.getEndpointOverride(LocalStackContainer.Service.S3);

    storage = new S3BinaryContentStorage(accessKey, secretKey, REGION.id(), BUCKET, EXPIRATION) {
      @Override
      public S3Client getS3Client() {
        return S3Client.builder()
            .endpointOverride(endpoint) // 동적 LocalStack 주소
            .region(REGION)
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)))
            .forcePathStyle(true)
            .build();
      }

      @Override
      public String generatePresignedUrl(String key, String contentType) {
        try (S3Presigner presigner = S3Presigner.builder()
            .endpointOverride(endpoint) // LocalStack 주소
            .region(REGION)
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)))
            .build()) {

          GetObjectRequest getObjectRequest = GetObjectRequest.builder()
              .bucket(BUCKET)
              .key(key)
              .build();

          GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
              .signatureDuration(Duration.ofSeconds(EXPIRATION))
              .getObjectRequest(getObjectRequest)
              .build();

          PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(
              presignRequest);
          return presignedGetObjectRequest.url().toString();
        }
      }
    };

    storage.getS3Client().createBucket(CreateBucketRequest.builder().bucket(BUCKET).build());

    id = UUID.randomUUID();
    content = "test content".getBytes();
  }


  @Test
  void testPutAndGet() throws IOException {
    storage.put(id, content);

    InputStream stream = storage.get(id);

    byte[] downloaded = stream.readAllBytes();

    Assertions.assertThat(downloaded).isEqualTo(content);
  }

  @Test
  void testDownloadPresignedUrl() throws Exception {

    BinaryContentDto dto = new BinaryContentDto(id, "test", 1L, "image/plain");
    ResponseEntity<?> response = storage.download(dto);

    Assertions.assertThat(response.getStatusCode().is3xxRedirection()).isTrue();

    String location = response.getHeaders().getLocation().toString();
    Assertions.assertThat(location.contains(id.toString())).isTrue();
  }
}
