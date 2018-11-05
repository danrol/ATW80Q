package main.java.playground.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message {
	private String message;
	private Date creationDate;
	private Location location;
	private Map<String, Object> moreAttributes;

	public Message() {
		this.creationDate = new Date();
		this.location = new Location(0, 0);
		this.moreAttributes = new HashMap<>();
		this.moreAttributes.put("creator", "AdvancedMessageGenerator");
		this.moreAttributes.put("length", 5);
		this.moreAttributes.put("validUntil", "forever");
	}

	public Message(String message) {
		this();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	@Override
	public String toString() {
		return message;
	}

}
