package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.user.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class CreateMessageFacadeImpl implements CreateMessageFacade{


  private final BinaryContentStorage binaryContentStorage;
  private final MessageService messageService;
  private final MessageMapper messageMapper;
  private final UserService userService;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentService binaryContentService;
  private final ChannelService channelService;
  private final ReadStatusService readStatusService;

  private final MessageAttachmentRepository messageAttachmentRepository;

  @Override
  @Transactional
  public MessageResponseDto createMessage(CreateMessageDto messageDto, List<MultipartFile> files) {

    Channel channel = channelService.findChannelById(messageDto.channelId());
    User user = userService.findUserById(messageDto.authorId());


    channelService.validateUserAccess(channel, user);

    Message message = messageMapper.toEntity(messageDto);
    message.addChannel(channel);
    message.addAuthor(user);


    List<BinaryContent> contents =  binaryContentMapper.fromMessageFiles(files);


    if(contents != null && !contents.isEmpty()){

      List<MessageAttachment> attachments = contents.stream()
          .map(content -> new MessageAttachment(message, content))
          .toList();

      // binaryContentService.saveBinaryContents(contents);

      for(MultipartFile file : files){
        try {
          BinaryContent content = contents.stream().filter(c -> c.getFileName() == file.getOriginalFilename()).findFirst().orElseThrow();
          binaryContentStorage.put(content.getId(), file.getBytes());
        }catch (IOException e){
          throw new CustomException(ErrorCode.FILE_ERROR);
        }
      }

      if(attachments != null && !attachments.isEmpty()) {
        message.getAttachments().addAll(attachments);
      }

    }


    messageService.createMessage(message);



    return messageMapper.toResponseDto(message);
  }
}
