package playground.logic.stubs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import playground.exceptions.ElementDataException;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


//@Service
public class DummyElementService implements ElementService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ArrayList<ElementEntity> elements = new ArrayList<ElementEntity>();

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public ArrayList<ElementEntity> getElements() {
		return elements;
	}

	public static void setElements(ArrayList<ElementEntity> elementsToSet) {
		elements = elementsToSet;
	}

	@Override
	public ElementEntity addElement(String userPlayground, String email, ElementEntity element) {
		return addElementNoLogin(element);
	}

	@Override
	public void addElements(String playground, String email, ElementEntity[] elements) {

		for (int i = 0; i < elements.length; i++) {
			addElement(playground, email,elements[i]);
		}
	}

	@Override
	public void addElementsNoLogin(ElementEntity[] elements) {
		for (int i = 0; i < elements.length; i++) {
			addElementNoLogin(elements[i]);
		}

	}

	@Override
	public ElementEntity getElement(String userPlayground, String email, String id, String CreatorPlayground) {

		return getElement(userPlayground, email, ElementEntity.createKey(id, CreatorPlayground));
	}

	@Override
	public ElementEntity getElementNoLogin(String superkey) {
		for (ElementEntity e : elements) {
			if (e.getSuperkey().equals(superkey))
				return e;
		}
		throw new RuntimeException("Could not find specified element (superkey=" + superkey);
	}

	@Override
	public ElementEntity getElement(String userPlayground, String email, String superkey) {
		return getElementNoLogin(superkey);
	}


	@Override
	public boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground,
			String creatorEmail) {
		if (element.getCreatorPlayground() == creatorPlayground && element.getCreatorEmail() == creatorEmail)
			return true;
		else
			return false;
	}



	@Override
	public ElementEntity[] lstToArray(ArrayList<ElementEntity> lst) {
		return lst.toArray(new ElementEntity[lst.size()]);
	}
	
	private ElementEntity[] paginateList(ArrayList<ElementEntity> lst, int page, int size) {
		return lst
				.stream()
				.skip(size * page) 
				.limit(size) 
				.collect(Collectors.toList())
				.toArray(new ElementEntity[lst.size()]); 
	}

	public void updateElementsInDatabase(String userPlayground, String email, ArrayList<ElementEntity> elements) {
		try {
			for (ElementEntity el : elements) {
				updateElementInDatabaseFromExternalElement(userPlayground, email,el);
			}

		} catch (ElementDataException e) {
			throw new ElementDataException("element in collection has fields that are incorrect.");
		}

	}

	@Override
	public void updateElementInDatabaseFromExternalElement(String userPlayground, String email,ElementEntity element) {
		userService.login(userPlayground, email);
		ElementEntity tempElement = this.getElement(userPlayground, email, element.getSuperkey());
		if (tempElement != null) {
			elements.remove(tempElement);
			elements.add(element);
		} else
			throw new ElementDataException("element data for update is incorrect");
	}

	@Override
	public void replaceElementWith(String userPlayground,
			String email, ElementEntity entity, String id, String creatorPlayground) {
		ElementEntity tempElement = this.getElement(userPlayground,
				email, ElementEntity.createKey(id, creatorPlayground));
		if (tempElement != null) {
			elements.remove(tempElement);
			elements.add(entity);
		} else
			throw new ElementDataException("element data for update is incorrect");

	}

	@Override
	public boolean isElementInDatabase(ElementEntity element) {
		if (elements.contains(element)) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public ElementEntity[] getAllElementsInRadius(String userPlayground, String email,double x, double y, double distance, Pageable pageable) {

		if (distance < 0) {
			throw new RuntimeException("Negative distance (" + distance + ")");
		}
		ArrayList<ElementEntity> lst = new ArrayList<>();
		for (ElementEntity el : elements) {
			double actualDistance = distanceBetween(el.getX(), el.getY(), x, y);
			if (actualDistance <= distance) {
				lst.add(el);
			}
		}
		if (lst.isEmpty()) {
			return null;
		} else {
			return paginateList(lst, pageable.getPageNumber(), pageable.getPageSize());
		}

	}



	@Override
	public void cleanElementService() {
		elements.clear();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public ElementEntity[] getAllElements() {
		ElementEntity[] arr = new ElementEntity[elements.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = elements.get(i);
		}
		return arr;

	}

	

	@Override
	public ElementEntity addElementNoLogin(ElementEntity element) {
		if (elements.contains(element))
			throw new ElementDataException("element data already exist in database");
		else {
		elements.add(element);
		return element;
		}
	}



	@Override
	public double distanceBetween(double x1, double y1, double x2, double y2) {
		double xin = x1 - x2;
		double yin = y1 - y2;
		return Math.sqrt(xin * xin + yin * yin);

	}


	@Override
	public ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String email,
			String attributeName, String value, Pageable pageable) {
		
		userService.login(creatorPlayground, email);
		ArrayList<ElementEntity> tempElementsList = new ArrayList<>();
		for (ElementEntity element : elements) {
			if (element.getAttributes().containsKey(attributeName)
					&& element.getAttributes().get(attributeName).equals(value))
				tempElementsList.add(element);
		}
		if (tempElementsList.isEmpty())
			return new ElementEntity[0];
		else
			return paginateList(tempElementsList, pageable.getPageNumber(), pageable.getPageSize());
	}

	@Override
	public ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, Pageable pageable) {
		ArrayList<ElementEntity> result = new ArrayList<>();
		for (ElementEntity element : elements) {
			if (checkEmailAndPlaygroundInElement(element, creatorPlayground, email))
				result.add(element);
		}
		return paginateList(result, pageable.getPageNumber(), pageable.getPageSize());
	}

	@Override
	public ArrayList<ElementEntity> getElements(Pageable pageable) {
		return new ArrayList<ElementEntity>(
				Arrays.asList(paginateList(getElements(), pageable.getPageNumber(), pageable.getPageSize())));
	}





}
