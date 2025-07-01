package com.roczyno.notificationservice.service;

import com.roczyno.notificationservice.model.Notification;
import com.roczyno.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private final NotificationRepository notificationRepository;

	public void saveNotification(Notification notification) {
		notificationRepository.save(notification);
	}

}
