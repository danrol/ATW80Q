package elements;

import java.util.Date;

import playground.Playground_constants;
import playground.logic.Location;


public class ElementTO implements Playground_constants {
	protected String name;
	protected String id;
	protected String playground;
	protected Date creationDate;
	protected Date expirationDate;
	protected String type;
	protected String creatorPlayground;
	protected String creatorEmail;
	protected Location location;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlayground() {
		return playground;
	}
	public void setPlayground(String playground) {
		this.playground = playground;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getExirationDate() {
		return expirationDate;
	}
	public void setExirationDate(Date exirationDate) {
		this.expirationDate = exirationDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreatorPlayground() {
		return creatorPlayground;
	}
	public void setCreatorPlayground(String creatorPlayground) {
		this.creatorPlayground = creatorPlayground;
	}
	public String getCreatorEmail() {
		return creatorEmail;
	}
	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}
	@Override
	public String toString() {
		return "ElementTO [name=" + name + ", id=" + id + ", playground=" + playground + ", creationDate="
				+ creationDate + ", exirationDate=" + expirationDate + ", type=" + type + ", creatorPlayground="
				+ creatorPlayground + ", creatorEmail=" + creatorEmail + ", location=" + location + "]";
	}
	
}
