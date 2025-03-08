package tmp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserApiTest {

  private final RestTemplate restTemplate = new RestTemplate();
  private static final String API_URL = "http://localhost:8080/api/users"; // API 엔드포인트
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testCreate500Users() throws Exception {
    List<UserResponseDto> createdUsers = new ArrayList<>();

    for (int i = 0; i < 500; i++) {
      // 사용자 요청 생성
      CreateUserRequest userRequest = new CreateUserRequest(
          "user" + i,
          "Password" + UUID.randomUUID(),
          "user" + i + "@example.com"
      );

      // Multipart 요청 데이터 생성
      MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

      // JSON 변환 후 추가
      String userJson = objectMapper.writeValueAsString(userRequest);
      requestBody.add("userCreateRequest", new ByteArrayResource(userJson.getBytes(StandardCharsets.UTF_8)) {
        @Override
        public String getFilename() {
          return "user.json";
        }
      });

      // 프로필 이미지 추가 (빈 파일)
      requestBody.add("profile", getEmptyFile());

      // HTTP 요청 헤더 설정
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

      // API 호출
      ResponseEntity<UserResponseDto> response = restTemplate.exchange(
          API_URL, HttpMethod.POST, requestEntity, UserResponseDto.class
      );

      // 응답 검증 및 저장
      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        createdUsers.add(response.getBody());
      }

      assertNotNull(response.getBody());
      System.out.println("Created User: " + response.getBody());
    }

    System.out.println("Successfully created " + createdUsers.size() + " users.");
  }

  private Resource getEmptyFile() {
    return new ByteArrayResource("".getBytes(StandardCharsets.UTF_8)) {
      @Override
      public String getFilename() {
        return "empty.txt";
      }
    };
  }
}
