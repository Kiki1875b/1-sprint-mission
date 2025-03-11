package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.MessageApiDocs;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.facade.message.MessageFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController implements MessageApiDocs {

  private final MessageFacade messageFacade;

  @Override
  @PostMapping(
      value = "/messages",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<MessageResponseDto> sendMessage(
      @RequestPart(value = "messageCreateRequest") CreateMessageDto messageDto,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> files){

    MessageResponseDto message = messageFacade.createMessage(messageDto, files);
    return ResponseEntity.status(HttpStatus.CREATED).body(message);
  }


  @Override
  @PatchMapping("/messages/{messageId}")
  public ResponseEntity<MessageResponseDto> updateMessage(@PathVariable String messageId, @RequestBody MessageUpdateDto messageDto){
    MessageResponseDto message = messageFacade.updateMessage(messageId, messageDto);
    return ResponseEntity.ok(message);
  }

  @Override
  @DeleteMapping("/messages/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable String messageId){
    messageFacade.deleteMessage(messageId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @GetMapping("/messages")
  public ResponseEntity<PageResponse<MessageResponseDto>> getChannelMessages(
      @RequestParam String channelId,
      @RequestParam(required = false) Instant cursor,
      @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){

    PageResponse<MessageResponseDto> messages = messageFacade.findMessagesByChannel(channelId, cursor, pageable);
    return ResponseEntity.ok(messages);
  }
}
