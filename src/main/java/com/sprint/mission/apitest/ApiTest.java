package com.sprint.mission.apitest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiTest {

  private static final String API_URL = "http://localhost:8080/api/users";
  private static final int NUM_REQUESTS = 1000;

  public static void main(String[] args) {
    HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();

    List<Long> responseTimes = new ArrayList<>();

    for (int i = 0; i < NUM_REQUESTS; i++) {
      Instant start = Instant.now();

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(API_URL))
          .GET()
          .timeout(Duration.ofSeconds(10))
          .build();

      try {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Instant end = Instant.now();
        long elapsedTime = Duration.between(start, end).toMillis();
        responseTimes.add(elapsedTime);

        // 응답 코드 확인
        if (response.statusCode() != 200) {
          System.err.println("❌ 요청 실패! 응답 코드: " + response.statusCode());
        }

      } catch (Exception e) {
        System.err.println("❌ 요청 중 오류 발생: " + e.getMessage());
      }
    }

    printStatistics(responseTimes);
  }

  private static void printStatistics(List<Long> responseTimes) {
    if (responseTimes.isEmpty()) {
      System.out.println("❌ 측정된 응답 시간이 없습니다.");
      return;
    }

    long sum = responseTimes.stream().mapToLong(Long::longValue).sum();
    double avgTime = sum / (double) responseTimes.size();
    long minTime = Collections.min(responseTimes);
    long maxTime = Collections.max(responseTimes);
    long p90 = responseTimes.get((int) (responseTimes.size() * 0.90));
    long p99 = responseTimes.get((int) (responseTimes.size() * 0.99));

    System.out.println("✅ API 성능 테스트 결과:");
    System.out.printf("▶ 평균 응답 시간: %.2f ms%n", avgTime);
    System.out.printf("▶ 최소 응답 시간: %d ms%n", minTime);
    System.out.printf("▶ 최대 응답 시간: %d ms%n", maxTime);
    System.out.printf("▶ 90th 퍼센타일: %d ms%n", p90);
    System.out.printf("▶ 99th 퍼센타일: %d ms%n", p99);
  }
}
