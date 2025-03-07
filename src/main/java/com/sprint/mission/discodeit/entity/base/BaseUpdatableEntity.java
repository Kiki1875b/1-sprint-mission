package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.Instant;

@MappedSuperclass
@Getter
public class BaseUpdatableEntity extends BaseEntity{

  @Column(name = "updated_at")
  private Instant updatedAt;

  @PreUpdate
  private void onUpdate(){
    this.updatedAt = Instant.now();
  }

}
