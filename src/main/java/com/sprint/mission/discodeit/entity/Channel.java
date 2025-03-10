package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChannelType type;
  private String name;
  private String description;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "channel")
  private List<ReadStatus> statuses = new ArrayList<>();

  // Channel channel = channelRepository.findbyid(); channel.getStatuses();
  // 한명의 사용자가 볼수있는 체널 목록 : 해당 유저가 참여중인 채널 + public -> private 인 경우, 모든 참여자를 조회
  // channel 과 user N:M -> channel_user -> read_status


  public enum ChannelType {
    PRIVATE, PUBLIC
  }


  public void updateChannelName(String channelName) {
    this.name = channelName;
  }

  public void updateDescription(String description) {
    this.description = description;
  }

  public void addReadStatus(ReadStatus readStatus) {
    if (statuses == null) {
      statuses = new ArrayList<>();
    }
    statuses.add(readStatus);
    readStatus.addChannel(this);
  }


  @Override
  public String toString() {
    return "Channel{" +
        "id='" + getId() + '\'' +
        ", channelType=" + type +
        ", channelName='" + name + '\'' +
        ", createdAt=" + getCreatedAt() +
        ", updatedAt=" + getUpdatedAt() +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Channel channel = (Channel) o;
    return Objects.equals(getId(), channel.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
