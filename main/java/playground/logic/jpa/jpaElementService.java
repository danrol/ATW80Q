package playground.logic.jpa;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import playground.Constants;
import playground.dal.ElementDao;
import playground.exceptions.ElementDataException;
import playground.logic.ElementEntity;
import playground.logic.ElementService;


@Service
public class jpaElementService implements ElementService {

	// this is the database we need are saving in
	private ElementDao elementsDB;

	@Autowired
	public jpaElementService(ElementDao elementsDB) {
		this.elementsDB = elementsDB;

	}

	@Override
	public void cleanElementService() {
		elementsDB.deleteAll();

	}

	public jpaElementService()
	{
		addDummyValues();
	}
	
	
	private void addDummyValues() {
		String playground="playground",creatorPlayground="creator",id="idOfElement";

		this.addElement(new ElementEntity(id,Constants.ELEMENT_NAME,playground, Constants.EMAIL_FOR_TESTS,1,2 ));

		this.addElement(new ElementEntity(id,Constants.ELEMENT_NAME,playground, Constants.EMAIL_FOR_TESTS,2,2));

		this.addElement(new ElementEntity(id,Constants.ELEMENT_NAME,playground, Constants.EMAIL_FOR_TESTS, 4,2));

		this.addElement(new ElementEntity(id,Constants.ELEMENT_NAME,playground, Constants.EMAIL_FOR_TESTS,5,2));
		
		
	}

	@Override
	@Transactional(readOnly = true)
	public ElementEntity[] getAllElementsInRadius(ElementEntity element, double x, double y, double distance, int page,
			int size) {
		if (distance < 0) {
			throw new RuntimeException("Negative distance (" + distance + ")");
		}
		ArrayList<ElementEntity> allElements = getElements();
		ArrayList<ElementEntity> lst = new ArrayList<>();
		for (ElementEntity el : allElements) {
			double xin = el.getX() - element.getX();
			double yin = el.getY() - element.getY();

			if (Math.sqrt(xin * xin + yin * yin) <= distance) {
				lst.add(el);
			}
		}
		if (lst.isEmpty()) {
			return null;
		} else {
			return getElementsBySizeAndPage(lst, page, size);
		}
	}

	@Override
	@Transient
	public boolean isElementInDatabase(ElementEntity element) {

		return this.elementsDB.existsById(element.getSuperkey());
	}

	@Override
	@Transactional(readOnly = false)
	public void addElements(ElementEntity[] elements, String userPlayground) {
		for (int i = 0; i < elements.length; i++) {
			addElement(elements[i]);
		}

	}

	@Override
	@Transactional(readOnly = false)
	public void updateElementInDatabaseFromExternalElement(ElementEntity element, String creatorEmail,
			String playground) {

		System.out.println("Perform update");
		System.out.println("Not updated element" + this.getElement(creatorEmail, playground));
		System.out.println("Updated element" + element);
		ElementEntity tempElement = this.getElement(creatorEmail, playground);
		printElementDB();
		if (tempElement != null) {
			System.out.println("Elemnt by id and string" + this.getElement(creatorEmail, playground).toString());
			System.out.println("temp element" + tempElement.toString());
			System.out.println("element" + element.toString());
			elementsDB.deleteById(tempElement.getSuperkey());
			elementsDB.save(element);
			printElementDB();
		} else
			throw new ElementDataException("element data for update is incorrect");

	}

	@Override
	@Transactional(readOnly = true)
	public ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail,
			String attributeName, String value, int page, int size) {

		ArrayList<ElementEntity> elements = getElements();
		ArrayList<ElementEntity> tempElementsList = new ArrayList<>();
		System.out.println("Entered get elements with value in attr");
		for (ElementEntity e : elements) {
			if (e.getCreatorPlayground().equals(creatorPlayground)
					&& e.getCreatorEmail().equals(creatorEmail)
					&& e.getAttributes().containsKey(attributeName)
					&& e.getAttributes().get(attributeName).equals(value))
				tempElementsList.add(e);
		}
		if (tempElementsList.isEmpty())
			return new ElementEntity[0];
		else
			return getElementsBySizeAndPage(tempElementsList, page, size);

	}

	@Override
	@Transient
	public boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground,
			String creatorEmail) {
		if (element.getCreatorPlayground().equals(creatorPlayground) && element.getCreatorEmail().equals(creatorEmail))
			return true;
		else
			return false;
	}

	@Override
	@Transactional(readOnly = true)
	public ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page,
			int size) {
		ArrayList<ElementEntity> elements = getElements();
		ArrayList<ElementEntity> result = new ArrayList<>();
		for (ElementEntity element : elements) {
			if (checkEmailAndPlaygroundInElement(element, creatorPlayground, email))
				result.add(element);
		}
		return getElementsBySizeAndPage(result, page, size);
	}

	@Override
	@Transactional(readOnly = true)
	public ElementEntity getElement(String email, String playground) {
		printElementDB();
		Optional<ElementEntity> el = elementsDB.findById(ElementEntity.setSuperkey(email, playground));
		if (el.isPresent()) {
			try {
				return el.get();
			} catch (Exception e) {
				System.out.println("element:" + el.toString() + "/n failed to load from database");
			}
		}
		return null;
	}
	
	public ElementEntity getElement(String superkey) {
		printElementDB();
		Optional<ElementEntity> el = elementsDB.findById(superkey);
		if (el.isPresent()) {
			try {
				return el.get();
			} catch (Exception e) {
				System.out.println("element:" + el.toString() + "/n failed to load from database");
			}
		}
		return null;
	}
	

	@Override
	@Transactional(readOnly = false)
	public void addElement(ElementEntity element) {
		if (elementsDB.existsById(element.getSuperkey())) {
			System.out.println("already exist in database:" + element.toString());
		} else {
				elementsDB.save(element);
		}

	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<ElementEntity> getElements() {
		ArrayList<ElementEntity> lst = new ArrayList<ElementEntity>();
			for(ElementEntity e : elementsDB.findAll())
				lst.add(e);

		return lst;

	}

	@Override
	@Transactional(readOnly = true)
	public ElementEntity[] getElementsBySizeAndPage(ArrayList<ElementEntity> lst, int page, int size) {
		return lst.stream().skip(size * page).limit(size).collect(Collectors.toList())
				.toArray(new ElementEntity[lst.size()]);

	}

	@Override
	@Transactional
	public void updateElementsInDatabase(ArrayList<ElementEntity> elements, String playground) {
		try {
			for (ElementEntity el : elements) {
				updateElementInDatabaseFromExternalElement(el, el.getCreatorEmail(), playground);
			}

		} catch (ElementDataException e) {
			throw new ElementDataException("Elements in collection have incorrect fields.");
		}

	}

	@Override
	@Transactional(readOnly = true)
	public ElementEntity[] getAllElements() {
		ArrayList<ElementEntity> arr = getElements();
		return arr.toArray(new ElementEntity[arr.size()]);
	}

	@Override
	@Transient
	public void printElementDB() {
		ArrayList<ElementEntity> lst = new ArrayList<ElementEntity>();

		for(ElementEntity e: elementsDB.findAll())
				lst.add(e);

		System.err.println("\nDB-TEST:all elements in database:\n");
		for (ElementEntity e : lst) {
			System.out.println(e.toString());
		}
		System.out.println("\n");
	}
}
