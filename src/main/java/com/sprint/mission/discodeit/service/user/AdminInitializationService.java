package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminInitializationService implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder encoder;

  @Override
  public void run(String... args) throws Exception {
    Optional<User> existing = userRepository.findByUsername("ADMIN");

    if (existing.isPresent()) {
      return;
    }

    User user = new User("ADMIN", "ADMIN", encoder.encode("ADMIN"), null, null,
        UserRole.ROLE_ADMIN);
    UserStatus status = new UserStatus(user, Instant.now());
    user.updateStatus(status);
    
    userRepository.save(user);
  }
}
