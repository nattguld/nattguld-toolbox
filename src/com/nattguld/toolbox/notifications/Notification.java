package com.nattguld.toolbox.notifications;

import com.notification.NotificationFactory.Location;
import com.utils.Time;

/**
 * 
 * @author randqm
 *
 */

public class Notification {
	
	/**
	 * The title.
	 */
	private String title;
	
	/**
	 * The sub title.
	 */
	private final String subTitle;
	
	/**
	 * The location.
	 */
	private Location location;
	
	/**
	 * The time.
	 */
	private Time time;
	
	
	/**
	 * Creates a new notification.
	 * 
	 * @param subTitle The sub title.
	 */
	public Notification(String title, String subTitle) {
		this.title = title;
		this.subTitle = subTitle;
		this.location = Location.SOUTHWEST;
		this.time = Time.seconds(1.5);
	}
	
	/**
	 * Modifies the title.
	 * 
	 * @param title The new title.
	 * 
	 * @return The notification.
	 */
	public Notification setTitle(String title) {
		this.title = title;
		return this;
	}
	
	/**
	 * Retrieves the title.
	 * 
	 * @return The title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Retrieves the sub title.
	 * 
	 * @return The sub title.
	 */
	public String getSubTitle() {
		return subTitle;
	}
	
	/**
	 * Modifies the location.
	 * 
	 * @param location The new location.
	 * 
	 * @return The notification.
	 */
	public Notification setLocation(Location location) {
		this.location = location;
		return this;
	}
	
	/**
	 * Retrieves the location.
	 * 
	 * @return The location.
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Modifies the time.
	 * 
	 * @param time The new time.
	 * 
	 * @return The notification.
	 */
	public Notification setTime(Time time) {
		this.time = time;
		return this;
	}
	
	/**
	 * Retrieves the time.
	 * 
	 * @return The time.
	 */
	public Time getTime() {
		return time;
	}

}
