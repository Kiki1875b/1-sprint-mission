package com.sprint.mission.discodeit.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_attachments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MessageAttachment {
  public MessageAttachment(Message message, BinaryContent attachment) {
    this.message = message;
    this.attachment = attachment;
    this.id = new MessageAttachmentId(message.getId(), attachment.getId());
  }

  @EmbeddedId
  private MessageAttachmentId id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @MapsId("messageId")
  @JoinColumn(name = "message_id", nullable = false)
  private Message message;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
  @MapsId("attachmentId")
  @JoinColumn(name = "attachment_id", nullable = false)
  private BinaryContent attachment;
}
