package playground.logic.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class IdGeneratorActivity {
	private Long id;

	public IdGeneratorActivity() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	/*
	 * TODO
	 * temporary fix - should be
	 * @GeneratedValue(strategy=GenerationType.IDENTITY)
	 * 
	 * */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
