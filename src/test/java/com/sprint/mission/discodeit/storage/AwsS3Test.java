package com.sprint.mission.discodeit.storage;


import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.storage.s3.S3Service;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class AwsS3Test {

  private static String accessKey;
  private static String secretKey;
  private static String bucket;
  private static String region;

  private String key = "1744009938630_test.txt";
  private static S3Service s3Service;

  @BeforeAll
  static void init() throws IOException {
    Properties properties = new Properties();
    properties.load(new FileInputStream(".env"));

    accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
    secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
    bucket = properties.getProperty("AWS_S3_BUCKET");
    region = properties.getProperty("AWS_S3_REGION");

    S3Client client = S3Client.builder()
        .region(Region.of(region))
        .build();

    s3Service = new S3Service(bucket, region, client);
  }

  @Test
  void testUpload() throws IOException {
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "test.txt",
        "text/plain",
        "S3 upload test".getBytes()
    );

    String result = s3Service.upload(file);
    key = result;
    assertThat(result).isNotEmpty();
  }

  @Test
  void testDownload() throws IOException {
    byte[] data = s3Service.download(key);
    assertThat(data).isNotEmpty();
    System.out.println("Downloaded content: " + new String(data));
  }

  @Test
  void testPresignedUrl() {
    String url = s3Service.generatePresignedUrl(key);
    assertThat(url).contains("https://");
    System.out.println("Presigned URL: " + url);
  }

}
