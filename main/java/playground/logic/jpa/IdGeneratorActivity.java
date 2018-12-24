package playground.logic.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class IdGeneratorActivity {
	private Long id;

	public IdGeneratorActivity() {
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		System.err.println("IN IdGeneratorElement : " + id);
		this.id = id;
	}
}
