package playground.logic;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import playground.Constants;


//KEY is: id+creatorPlayground
@Entity
@Table(name = "ELEMENT")
public class ElementEntity {

	private static int ID = 0;

	private static final long serialVersionUID = 1L;
	private String name;
	private String id;
	private String playground;
	private Date creationDate;
	private Date expirationDate;
	private String type = Constants.ELEMENT_DEFAULT_TYPE;
	private String creatorPlayground;
	private String creatorEmail;
	private double x;
	private double y;
//	private Location location = new Location();
	private Map<String, Object> attributes = Collections.synchronizedMap(new HashMap<>());
	private String superkey;



	public ElementEntity() {
//		this.location = new Location();
//		id = ID++;
		this.expirationDate = new Date(Constants.DEFAULT_EXPIRATION_YEAR,Constants.DEFAULT_EXPIRATION_MONTH, Constants.DEFAULT_EXPIRATION_DAY);
		this.creationDate = new Date();
	}

//	create constructors that receive just a JSon 
//	string and create the class in ElementEntity
	public ElementEntity(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ElementEntity elEntity = objectMapper.readValue(jsonString, ElementEntity.class);
		this.name = elEntity.name;
		this.id = elEntity.id;
		this.playground = elEntity.playground;
		this.creationDate = elEntity.creationDate;
		this.creatorPlayground = elEntity.creatorPlayground;
		this.creatorEmail = elEntity.creatorEmail;
		setX(elEntity.getX());
		setY(elEntity.getY());
		setSuperkey();


	}

	public ElementEntity(String id,String name, String playground, String email, double x, double y) {
		// this constructor is used for
		// /playground/elements/{userPlayground}/{email}/{playground}/{id}
		// which won't pass expirationDate, name, type and location
		// TODO add expirationDate, location implementation
		
		this();
		this.id = id;
		this.name = name;
		this.playground = playground;
		this.creatorPlayground = Constants.PLAYGROUND_NAME;
		this.creatorEmail = email;
		setX(x);
		setY(y);
		setSuperkey();
	}
	
	
	public boolean attributeExists(String attributeName, String value) {
		switch (attributeName) {
		case "name":
			return name.equals(value);
		case "id":
			return id.equals(value);
		case "playground":
			return playground.equals(value);
		case "type":
			return type.equals(value);
		case "creatorPlayground":
			return creatorPlayground.equals(value);
		case "creatorEmail":
			return creatorEmail.equals(value);

		case "creationDate":
			return (new Date(value)).equals(creationDate);
		case "expirationDate":
			return (new Date(value)).equals(expirationDate);
//		case "location":
//			return (new Location(value)).equals(location);
		}
		return false;
	}
	
	
	@Id
	public String getSuperkey() {
		return superkey;
	}

	public void setSuperkey(String Superkey) {
		this.superkey =Superkey ;
	}
	
	public void setSuperkey() {
		superkey = setSuperkey(id, creatorPlayground);
	}
	
	@Transient
	public static String setSuperkey(String string1, String string2) {
		return string1.concat(" " + string2);
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
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
//		this.creationDate = creationDate;
		this.creationDate = new Date(creationDate.getTime());
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
//		this.expirationDate = expirationDate;
		this.expirationDate = new Date(expirationDate.getTime());
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
	@Transient
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	@Lob
	public String getJsonAttributes() {
		try {
			return new ObjectMapper().writeValueAsString(attributes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//map translation to json for the database
	public void setJsonAttributes(String jsonAttributes) {
		try {
			this.attributes = new ObjectMapper().readValue(jsonAttributes, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x =x;
	}
	
	public double getY() {
		return y;
}

	public void setY(double y) {
		this.y = y;
	}
	@Transient
	public String toString() {
		return "ElementEntity [superkey="+superkey+", name=" + name + ", id=" + id + ", playground=" + playground + ", creationDate="
				+ creationDate + ", expirationDate=" + expirationDate + ", type=" + type + ", creatorPlayground="
				+ creatorPlayground + ", attributes="+attributes.toString()+", creatorEmail=" + creatorEmail + ", x= " + x + " y="+y+"]";
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}