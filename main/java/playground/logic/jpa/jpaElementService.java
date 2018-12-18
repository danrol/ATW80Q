package playground.logic.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import playground.aop.LoginRequired;
import playground.aop.MyLog;
import playground.dal.ElementDao;
import playground.exceptions.ElementDataException;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserService;

@Service
public class jpaElementService implements ElementService {

	// this is the database we need are saving in
	private ElementDao elementsDB;

	private UserService userService;

	@Autowired
	public jpaElementService(ElementDao elementsDB) {
		this.elementsDB = elementsDB;

	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	@MyLog
	public void cleanElementService() {
		elementsDB.deleteAll();

	}

	public jpaElementService() {
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public ElementEntity[] getAllElementsInRadius(double x, double y, double distance, int page, int size,
			String userPlayground, String email) {
		userService.login(userPlayground, email);

		if (distance < 0) {
			throw new RuntimeException("Negative distance (" + distance + ")");
		}
		ArrayList<ElementEntity> allElements = getElements();
		ArrayList<ElementEntity> lst = new ArrayList<>();

		for (ElementEntity el : allElements) {
			double actualDistance = distanceBetween(el.getX(), el.getY(), x, y);
			if (actualDistance <= distance) {
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
	@MyLog
	public double distanceBetween(double x1, double y1, double x2, double y2) {
		double xin = x1 - x2;
		double yin = y1 - y2;
		return Math.sqrt(xin * xin + yin * yin);

	}

	@Override
	@Transient
	@MyLog
	public boolean isElementInDatabase(ElementEntity element) {

		return this.elementsDB.existsById(element.getSuperkey());
	}

	@Override
	@Transactional(readOnly = false)
	public void addElements(ElementEntity[] elements, String userPlayground, String email) {
		for (int i = 0; i < elements.length; i++) {
			addElement(elements[i], userPlayground, email);
		}

	}

	@Override
	@Transactional(readOnly = false)
	@MyLog
	public void addElementsNoLogin(ElementEntity[] elements) {
		for (int i = 0; i < elements.length; i++) {
			addElementNoLogin(elements[i]);
		}

	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	//@LoginRequired
	public ElementEntity[] getElementsWithValueInAttribute(String userPlayground, String email,
			String attributeName, String value, int page, int size) {
		System.err.println("here \n" + userPlayground + " " + email+" "+attributeName+" "+size);
		ArrayList<ElementEntity> elements = getElements();
		ArrayList<ElementEntity> tempElementsList = new ArrayList<>();
		for (ElementEntity e : elements) {
			if (e.getAttributes().containsKey(attributeName) && e.getAttributes().get(attributeName).equals(value))
				tempElementsList.add(e);
		}
		if (tempElementsList.isEmpty())
		{
			return new ElementEntity[0];
		}
		else
			return getElementsBySizeAndPage(tempElementsList, page, size);

	}

	@Override
	@Transient
	@MyLog
	public boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground,
			String creatorEmail) {
		if (element.getCreatorPlayground().equals(creatorPlayground) && element.getCreatorEmail().equals(creatorEmail))
			return true;
		else
			return false;
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
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
	public ElementEntity getElement(String id, String creatorPlayground, String userPlayground, String email) {
		return getElement(ElementEntity.createKey(id, creatorPlayground), userPlayground, email);
	}

	@Override
	@Transactional(readOnly = true)
	public ElementEntity getElement(String superkey, String userPlayground, String email) {

		userService.login(userPlayground, email);
		return getElementNoLogin(superkey);
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public ElementEntity getElementNoLogin(String superkey) {
		Optional<ElementEntity> el = elementsDB.findById(superkey);
		if (el.isPresent()) {
			System.err.println("\n\n\n");
			try {
				ElementEntity t = el.get();
				return t;
			} catch (Exception e) {
				System.err.println("element:" + el.toString() + "/n failed to load from database");
			}
		}
		System.err.println("Could not find element " + superkey);
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public void addElement(ElementEntity element, String userPlayground, String email) {
		userService.login(userPlayground, email);

		addElementNoLogin(element);

	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<ElementEntity> getElements() {
		ArrayList<ElementEntity> lst = new ArrayList<ElementEntity>();
		for (ElementEntity e : elementsDB.findAll())
			lst.add(e);

		return lst;

	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public ElementEntity[] getElementsBySizeAndPage(ArrayList<ElementEntity> lst, int page, int size) {
		return lst.stream().skip(size * page).limit(size).collect(Collectors.toList())
				.toArray(new ElementEntity[page]);

	}

	@Override
	@Transactional
	@MyLog
	public void updateElementsInDatabase(ArrayList<ElementEntity> elements, String userPlayground, String email) {
		try {
			for (ElementEntity el : elements) {
				updateElementInDatabaseFromExternalElement(el, userPlayground, email);
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

		for (ElementEntity e : elementsDB.findAll())
			lst.add(e);

		System.err.println("\nDB-TEST:all elements in database:\n");
		for (ElementEntity e : lst) {
			System.out.println(e.toString());
		}
		System.out.println("\n");
	}

	@Override
	@MyLog
	public void addElementNoLogin(ElementEntity element) {
		printElementDB();
		if (elementsDB.existsById(element.getSuperkey())) {
			System.out.println("already exist in database:" + element.toString());
		} else {
			elementsDB.save(element);
		}
	}

	@Override
	@MyLog
	public void replaceElementWith(ElementEntity entity, String id, String creatorPlayground, String userPlayground,
			String email) {
		userService.login(userPlayground, email);
		ElementEntity tempElement = this.getElement(ElementEntity.createKey(id, creatorPlayground), userPlayground,
				email);
		if (tempElement != null) {
			// Deletes old and replaces with new
			elementsDB.deleteById(tempElement.getSuperkey());
			elementsDB.save(entity);
		} else
			throw new ElementDataException("element data for update is incorrect");

	}

	@Override
	@Transactional(readOnly = false)
	@MyLog
	public void updateElementInDatabaseFromExternalElement(ElementEntity element, String userPlayground, String email) {
		userService.login(userPlayground, email);
		ElementEntity tempElement = this.getElement(element.getSuperkey(), userPlayground, email);
		if (tempElement != null) {
			// Deletes old and replaces with new
			elementsDB.deleteById(tempElement.getSuperkey());
			elementsDB.save(element);
		} else
			throw new ElementDataException("element data for update is incorrect");

	}

}
