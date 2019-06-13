package com.nattguld.toolbox.notifications;

import java.net.URL;
import java.util.Objects;

import com.nattguld.util.audio.Audio;
import com.nattguld.util.audio.AudioManager;
import com.notification.NotificationFactory;
import com.notification.NotificationManager;
import com.notification.manager.SimpleManager;
import com.notification.types.TextNotification;
import com.theme.ThemePackagePresets;

/**
 * 
 * @author randqm
 *
 */

public class NotificationsManager {
	
	/**
	 * The factory.
	 */
	private static final NotificationFactory FACTORY = new NotificationFactory(ThemePackagePresets.cleanDark());
	
	private static final URL DEFAULT_AUDIO = NotificationManager.class.getResource("/res/audio/default_notification.wav");
	
	
	/**
	 * Sends a default notification.
	 * 
	 * @param notification The notification.
	 */
	public static void sendNotification(Notification notification) {
		sendNotification(notification, new Audio(DEFAULT_AUDIO));
	}
	
	/**
	 * Sends a notification.
	 * 
	 * @param notification The notification.
	 * 
	 * @param audio The audio to play.
	 */
	public static void sendNotification(Notification notification, Audio audio) {
		NotificationManager plain = new SimpleManager(notification.getLocation());
		
		TextNotification textNotification = FACTORY.buildTextNotification(notification.getTitle(), notification.getSubTitle());
		textNotification.setCloseOnClick(true);

		plain.addNotification(textNotification, notification.getTime());
		
		if (Objects.nonNull(audio)) {
			AudioManager.submit(audio);
		}
	}

}
