package playground.logic;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import playground.Constants;

@Entity
@Table(name = "ACTIVITY")
public class ActivityEntity implements Serializable {

	// Primary key - playground+id
	private static final long serialVersionUID = 514354009958930154L;
	private String playground;
	private String id="";
	private String elementPlayground;
	private String elementId;
	private String type;
	private String playerPlayground;
	private String playerEmail;
	private String superkey;
	private Map<String, Object> attribute;

	private ElementService elementService;
	private UserService userService;

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ActivityEntity() {
		attribute = new HashMap<String, Object>();
		this.type = Constants.DEFAULT_ACTIVITY_TYPE;
		this.playground = Constants.PLAYGROUND_NAME;
	}

//	create constructors that receive just a JSon 
//	string and create the class in ActivityEntity
	public ActivityEntity(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ActivityEntity acEntity = objectMapper.readValue(jsonString, ActivityEntity.class);
		this.id = acEntity.id;
		this.playground = acEntity.playground;
		this.elementPlayground = acEntity.elementPlayground;
		this.elementId = acEntity.elementId;
		this.type = acEntity.type;
		this.playerPlayground = acEntity.playerPlayground;
		this.playerEmail = acEntity.playerEmail;
		this.attribute = acEntity.attribute;

	}

	@Override
	public String toString() {
		return "ActivityTO [toString()=" + super.toString() + "]";
	}

	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getElementPlayground() {
		return elementPlayground;
	}

	public void setElementPlayground(String elementPlayground) {
		this.elementPlayground = elementPlayground;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlayerPlayground() {
		return playerPlayground;
	}

	public void setPlayerPlayground(String playerPlayground) {
		this.playerPlayground = playerPlayground;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	@Transient
	public Map<String, Object> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	@Lob
	// large object - can take as much space as it needs in the computer
	public String getJsonAttributes() {
		try {
			return new ObjectMapper().writeValueAsString(this.attribute);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setJsonAttributes(String jsonAttributes) {
		try {
			this.attribute = new ObjectMapper().readValue(jsonAttributes, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Id
	public String getSuperkey() {
		return createKey(id, playground);
	}

	public void setSuperkey(String Superkey) {
		//empty
	}
	@Transient
	public void setSuperkey() {
		//empty
		}
	@Transient
	public static String createKey(String id, String playground) {
		return id.concat(" " + playground);
	}
}
