package com.sprint.mission.discodeit.service.notification;


import com.sprint.mission.discodeit.async.AsyncTaskFailure;
import com.sprint.mission.discodeit.async.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.event_entity.NotificationEvent;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationMapper notificationMapper;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final AsyncTaskFailureRepository failureRepository;

  @Transactional(readOnly = true)
  public List<NotificationDto> getUserNotifications(User user) {
    List<Notification> notifications = notificationRepository.findByReceiver(user);

    return Collections.unmodifiableList(notificationMapper.toDtoList(notifications));
  }

  @Retryable(
      value = Exception.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000)
  )
  @Transactional
  public List<NotificationDto> createNotificationsFromEvent(NotificationEvent event) {

    if (true) {
      throw new RuntimeException();
    }

    List<Notification> notifications = event.receivers().stream()
        .map(user -> notificationMapper.toEntityFromEvent(event, user))
        .collect(Collectors.toList());

    notificationRepository.saveAll(notifications);

    return Collections.unmodifiableList(notificationMapper.toDtoList(notifications));
  }

  @Recover
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<NotificationDto> recoverNotificationFailure(Exception e, NotificationEvent event) {
    AsyncTaskFailure failure = new AsyncTaskFailure();

    failure.setFields(
        "NotificationService#createNotification",
        MDC.get("requestId"),
        e.getMessage() != null ? e.getMessage() : "ERROR"
    );

    failureRepository.save(failure);
    return Collections.emptyList();
  }
}
