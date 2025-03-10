package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

   @EntityGraph(attributePaths = {"profile", "status"})
   Optional<User> findById(UUID id);

  @EntityGraph(attributePaths = {"profile", "status"})
  List<User> findAll();

  @EntityGraph(attributePaths = {"profile", "status"})
  List<User> findAllByIdIn(List<UUID> userIds);

  Optional<User> findByUsername(String username);

  @Query("""
       SELECT u FROM User u
       LEFT JOIN FETCH u.profile
       LEFT JOIN FETCH u.status
       WHERE u.username = :username
      """)
  Optional<User> findByUsernameWithProfileAndStatus(@Param("username") String username);

  @Query("""
          SELECT u FROM User u
          LEFT JOIN FETCH u.profile
          WHERE u.username = :username
      """)
  Optional<User> findByUsernameWithProfile(@Param("username") String username);
}
