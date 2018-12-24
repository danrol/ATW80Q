package playground.logic.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import playground.Constants;
import playground.aop.PlayerLogin;
import playground.aop.LoginRequired;
import playground.aop.ModeratorLogin;
import playground.aop.MyLog;
import playground.dal.ElementDao;
import playground.exceptions.ElementDataException;
import playground.exceptions.RegisterNewUserException;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;

@Service
public class jpaElementService implements ElementService {

	// this is the database we need are saving in
	private ElementDao elementsDB;
	private IdGeneratorElementDao idGeneratorElement;
	private UserService userService;

	@Autowired
	public jpaElementService(ElementDao elementsDB, IdGeneratorElementDao idGeneratorElement) {
		this.elementsDB = elementsDB;
		this.idGeneratorElement = idGeneratorElement;
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
	
	@Override
	@Transactional(readOnly = true)
	@MyLog
	@PlayerLogin
	public ElementEntity[] getAllElementsInRadius(String userPlayground, String email, double x, double y, double distance, Pageable pageable) {

		if (distance < 0) {
			throw new RuntimeException("Negative distance (" + distance + ")");
		}
		ArrayList<ElementEntity> allElements = getElements(pageable);
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
			return lstToArray(lst);
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
	@ModeratorLogin
	public void addElements(String userPlayground, String email, ElementEntity[] elements) {
		for (int i = 0; i < elements.length; i++) {
			addElement(userPlayground, email, elements[i]);
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
	@PlayerLogin
	public ElementEntity[] getElementsWithValueInAttribute(String userPlayground, String email,
			String attributeName, String value, Pageable pageable) {
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
			return lstToArray(tempElementsList);

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
	public ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, Pageable pageable) {
		ArrayList<ElementEntity> elements = getElements();
		ArrayList<ElementEntity> result = new ArrayList<>();
		for (ElementEntity element : elements) {
			if (checkEmailAndPlaygroundInElement(element, creatorPlayground, email))
				result.add(element);
		}
		return lstToArray(result);
	}

	@Override
	@Transactional(readOnly = true)
	@LoginRequired
	public ElementEntity getElement(String userPlayground, String email, String id, String creatorPlayground) {
		return getElement(userPlayground, email,ElementEntity.createKey(id, creatorPlayground));
	}

	@Override
	@Transactional(readOnly = true)
	@LoginRequired
	public ElementEntity getElement(String userPlayground, String email, String superkey) {
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
	@LoginRequired
	public void addElement(String userPlayground, String email, ElementEntity element) {
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
	public ArrayList<ElementEntity> getElements(Pageable pageable) {
		ArrayList<ElementEntity> lst = new ArrayList<ElementEntity>();
		for (ElementEntity e : elementsDB.findAll(pageable))
			lst.add(e);

		return lst;
	}
	

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public ElementEntity[] lstToArray(ArrayList<ElementEntity> lst) {
		return lst.toArray(new ElementEntity[lst.size()]);

	}

	@Override
	@Transactional
	@MyLog
	@ModeratorLogin
	public void updateElementsInDatabase(String userPlayground, String email, ArrayList<ElementEntity> elements) {
		try {
			for (ElementEntity el : elements) {
				updateElementInDatabaseFromExternalElement(userPlayground, email,el);
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
	@MyLog
	public void addElementNoLogin(ElementEntity element) {
		
		if (elementsDB.existsById(element.getSuperkey())) {
			System.out.println("already exist in database: " + element.toString());
		} else {
			IdGeneratorElement tmp = this.idGeneratorElement.save(new IdGeneratorElement());
			System.err.println("Element ID: " + tmp.getId());
			Long dummyId = tmp.getId();
			this.idGeneratorElement.delete(tmp);
			element.setId("" + dummyId);
			elementsDB.save(element);
		}
	}
	
	
	@Override
	@MyLog
	@ModeratorLogin
	public void replaceElementWith(String userPlayground,
			String email, ElementEntity entity, String id, String creatorPlayground) {
		ElementEntity tempElement = this.getElement(userPlayground,
				email,ElementEntity.createKey(id, creatorPlayground));
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
	@ModeratorLogin
	public void updateElementInDatabaseFromExternalElement(String userPlayground, String email,ElementEntity element) {
		
		ElementEntity tempElement = this.getElement(userPlayground, email, element.getSuperkey());
		if (tempElement != null) {
			// Deletes old and replaces with new
			elementsDB.deleteById(tempElement.getSuperkey());
			elementsDB.save(element);
		} else
			throw new ElementDataException("element data for update is incorrect");

	}


}
