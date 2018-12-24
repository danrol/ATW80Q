package playground.logic.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class IdGeneratorUser {
	private Long id;

	public IdGeneratorUser() {
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		System.err.println("IN IdGeneratorUser : " + id);
		this.id = id;
	}

}
