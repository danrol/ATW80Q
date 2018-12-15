package playground.layout;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import playground.logic.ElementEntity;
import playground.logic.Location;

public class ElementTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String name;
	protected String id;
	protected String playground;
	protected Date creationDate;
	protected Date expirationDate;
	protected String type;
	protected String creatorPlayground;
	protected String creatorEmail;
	protected Location location;
	protected Map<String, Object> attributes;

	

	public ElementTO(ElementEntity e) {
		this.setName(e.getName());
		this.setId(e.getId());
		this.setPlayground(e.getPlayground());
		this.setCreationDate(e.getCreationDate());
		this.setExpirationDate(e.getExpirationDate());
		this.setType(e.getType());
		this.setCreatorPlayground(e.getCreatorPlayground());
		this.setCreatorEmail(e.getCreatorEmail());
		this.setLocation(new Location(e.getX(), e.getY()));
		this.setAttributes(e.getAttributes());
	}

	public ElementTO() {

	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

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

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
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
				+ creationDate + ", expirationDate=" + expirationDate + ", type=" + type + ", creatorPlayground="
				+ creatorPlayground + ", creatorEmail=" + creatorEmail + ", location=" + location + "]";
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ElementEntity toEntity() {
		ElementEntity rv = new ElementEntity();
		// need to think the logic
		rv.setName(name);
		rv.setId(id);
		rv.setPlayground(playground);
		rv.setCreationDate(creationDate);
		rv.setExpirationDate(expirationDate);
		rv.setType(type);
		rv.setCreatorPlayground(creatorPlayground);
		rv.setCreatorEmail(creatorEmail);
		rv.setX(location.getX());
		rv.setY(location.getY());
		rv.setAttributes(attributes);
		rv.setSuperkey();
		return rv;
	}

}
