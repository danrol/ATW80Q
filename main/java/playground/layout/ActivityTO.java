package playground.layout;

import java.util.HashMap;
import java.util.Map;

import playground.logic.ActivityEntity;


public class ActivityTO  {

	private String playground;
	private String id;
	private String elementPlayground;
	private String elementId;
	private String type;
	private String playerPlayground;
	private String playerEmail;
	private Map<String, Object> attribute;

	public ActivityTO() {
		attribute = new HashMap<String, Object>();
	}

	public ActivityTO(ActivityEntity a) {
		playground = a.getPlayground();
		id = a.getId();
		elementPlayground = a.getElementPlayground();
		elementId = a.getElementId();
		type = a.getType();
		playerPlayground = a.getPlayerPlayground();
		playerEmail = a.getPlayerEmail();
		attribute = a.getAttribute();
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

	public Map<String, Object> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	public ActivityEntity toEntity() {
		ActivityEntity rv = new ActivityEntity();
		rv.setAttribute(this.getAttribute());
		rv.setElementId(this.getElementId());
		rv.setElementPlayground(this.getElementPlayground());
		rv.setId(this.getId());
		rv.setPlayerEmail(this.getPlayerEmail());
		rv.setPlayerPlayground(this.getPlayerPlayground());
		rv.setPlayground(this.getPlayground());
		rv.setType(this.getType());
		return rv;
	}

	@Override
	public String toString() {
		return "ActivityTO [playground=" + playground + ", id=" + id + ", elementPlayground=" + elementPlayground
				+ ", elementId=" + elementId + ", type=" + type + ", playerPlayground=" + playerPlayground
				+ ", playerEmail=" + playerEmail + ", attribute=" + attribute + "]";
	}
	
	
}
