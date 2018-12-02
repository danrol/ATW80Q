package playground.logic;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Id;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import playground.Constants;

public class ElementEntity{

	private static final long serialVersionUID = 1L;
	protected String name;
	protected String id;
	protected String playground;
	protected Date creationDate;
	protected Date expirationDate=null;
	protected String type = Constants.ELEMENT_DEFAULT_TYPE;
	protected String creatorPlayground;
	protected String creatorEmail;
	protected Location location;
	protected Map<String, Object>attributes = Collections.synchronizedMap(new HashMap<>()); 
	
	public boolean attributeExists(String attributeName, String value) {
		switch(attributeName)
		{
		case "name": return name.equals(value);
		case "id": return id.equals(value);
		case "playground": return playground.equals(value);
		case "type": return type.equals(value);
		case "creatorPlayground": return creatorPlayground.equals(value);
		case "creatorEmail": return creatorEmail.equals(value);
		
		case "creationDate": return (new Date(value)).equals(creationDate);
		case "expirationDate": return (new Date(value)).equals(expirationDate);
		case "location": return (new Location(value)).equals(location);
		}
		return false;
	}
	
	public ElementEntity() {
		
	}
	
//	create constructors that receive just a JSon 
//	string and create the class in ElementEntity
	public ElementEntity(String jsonString) throws JsonParseException, JsonMappingException, IOException {
			ObjectMapper objectMapper = new ObjectMapper();
			ElementEntity elEntity = objectMapper.readValue(jsonString, ElementEntity.class); 
			this.id = elEntity.id;
			this.playground = elEntity.playground;
			this.creationDate = elEntity.creationDate;
			this.creatorPlayground = elEntity.creatorPlayground;
			this.creatorEmail = elEntity.creatorEmail;
			setLocation(elEntity.location);
		
	}
	
	public ElementEntity(String id, String playground, String email, Location xy) {
		// this constructor is used for /playground/elements/{userPlayground}/{email}/{playground}/{id} 
		//which won't pass expirationDate, name, type and location
		// TODO add expirationDate, location implementation
		super();
		this.id = id;
		this.playground = playground;
		this.creationDate = new Date();
		this.creatorPlayground = Constants.PLAYGROUND_NAME;
		this.creatorEmail = email;
		setLocation(xy);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Id
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
		if(location != null)
			this.location = location;
		else
			throw new RuntimeException("Location is null");
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
	public void setExpirationDate(Date exirationDate) {
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
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public String toString() {
		return "ElementEntity [name=" + name + ", id=" + id + ", playground=" + playground + ", creationDate="
				+ creationDate + ", exirationDate=" + expirationDate + ", type=" + type + ", creatorPlayground="
				+ creatorPlayground + ", creatorEmail=" + creatorEmail + ", location=" + location + "]";
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
