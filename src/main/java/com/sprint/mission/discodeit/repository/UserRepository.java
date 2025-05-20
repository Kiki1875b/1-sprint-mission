package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

  @EntityGraph(attributePaths = {"profile", "status"})
  Optional<User> findById(UUID id);

  @EntityGraph(attributePaths = {"profile", "status"})
  List<User> findAll();

  @EntityGraph(attributePaths = {"profile", "status"})
  List<User> findAllByIdIn(List<UUID> userIds);

  @Query("""
       SELECT u FROM User u
       LEFT JOIN FETCH u.profile
       LEFT JOIN FETCH u.status
       WHERE u.username = :username
      """)
  Optional<User> findByUsernameWithProfileAndStatus(@Param("username") String username);

  Optional<User> findByUsername(String username);
}
