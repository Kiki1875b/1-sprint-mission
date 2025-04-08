package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.storage.s3.S3BinaryContentStorage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

/**
 * localstack/localstack 이미지로 컨테이너를 띄우거나 s3localstack.sh 를 실행해야 합니다.
 */
public class S3BinaryContentStorageTest {

  private static String accessKey;
  private static String secretKey;
  private static String region;
  private static String bucket;
  private static Integer expirationTime;

  private static S3BinaryContentStorage storage;
  private static UUID id;
  private static byte[] content;

  @BeforeAll
  static void init() throws Exception {
    Properties properties = new Properties();
    properties.load(new FileInputStream(".env.s3test"));

    accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
    secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
    bucket = properties.getProperty("AWS_S3_BUCKET");
    region = properties.getProperty("AWS_S3_REGION");
    expirationTime = Integer.parseInt(properties.getProperty("AWS_S3_PRESIGNED_URL_EXPIRATION"));

    storage = new S3BinaryContentStorage(accessKey, secretKey, region, bucket, expirationTime) {
      @Override
      public S3Client getS3Client() {
        return S3Client.builder()
            .endpointOverride(URI.create("http://localhost:4566")) // LocalStack 주소
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)))
            .forcePathStyle(true)
            .build();
      }

      @Override
      public String generatePresignedUrl(String key, String contentType) {
        try (S3Presigner presigner = S3Presigner.builder()
            .endpointOverride(URI.create("http://localhost:4566")) // LocalStack 주소
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)))
            .build()) {

          GetObjectRequest getObjectRequest = GetObjectRequest.builder()
              .bucket(bucket)
              .key(key)
              .build();

          GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
              .signatureDuration(Duration.ofSeconds(expirationTime))
              .getObjectRequest(getObjectRequest)
              .build();

          PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(
              presignRequest);
          return presignedGetObjectRequest.url().toString();
        }
      }
    };

    storage.getS3Client().createBucket(CreateBucketRequest.builder().bucket(bucket).build());

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
    ResponseEntity<?> response = storage.download(id);

    Assertions.assertThat(response.getStatusCode().is3xxRedirection()).isTrue();

    String location = response.getHeaders().getLocation().toString();
    Assertions.assertThat(location.contains(id.toString())).isTrue();
  }
}
