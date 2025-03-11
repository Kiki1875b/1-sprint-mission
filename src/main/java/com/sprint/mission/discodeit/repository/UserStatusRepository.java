package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

//  @Query("""
//    SELECT us FROM UserStatus us JOIN FETCH us.user WHERE us.user.id = :userId
//      """)
  Optional<UserStatus> findByUser_Id(UUID userId);

}
