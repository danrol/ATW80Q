package playground.logic.stubs;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import playground.exceptions.ElementDataException;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserService;

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
	public void addElement(ElementEntity element, String userPlayground, String email) {
		userService.login(userPlayground, email);
		addElementNoLogin(element);
	}

	@Override
	public void addElements(ElementEntity[] elements, String playground, String email) {

		for (int i = 0; i < elements.length; i++) {
			addElement(elements[i], playground, email);
		}

	}
	
	@Override
	public void addElementsNoLogin(ElementEntity[] elements) {
		for (int i = 0; i < elements.length; i++) {
			addElementNoLogin(elements[i]);
		}
		
	}

	@Override
	public ElementEntity getElement(String id, String CreatorPlayground, String userPlayground, String email) {

		return getElement(ElementEntity.setSuperkey(id, CreatorPlayground), userPlayground, email);
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
	public ElementEntity getElement(String superkey, String userPlayground, String email) {
		userService.login(userPlayground, email);
		return getElementNoLogin(superkey);
	}

	@Override
	public ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page,
			int size) {
		ArrayList<ElementEntity> result = new ArrayList<>();
		for (ElementEntity element : elements) {
			if (checkEmailAndPlaygroundInElement(element, creatorPlayground, email))
				result.add(element);
		}
		return getElementsBySizeAndPage(result, page, size);
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
	public ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail,
			String attributeName, String value, int page, int size) {
		ArrayList<ElementEntity> tempElementsList = new ArrayList<>();
		for (ElementEntity element : elements) {
			if (element.getAttributes().containsKey(attributeName)
					&& element.getAttributes().get(attributeName).equals(value))
				tempElementsList.add(element);
		}
		if (tempElementsList.isEmpty())
			return new ElementEntity[0];
		else
			return getElementsBySizeAndPage(tempElementsList, page, size);
	}

	// return arrays values depending on page and size
	@Override
	public ElementEntity[] getElementsBySizeAndPage(ArrayList<ElementEntity> lst, int page, int size) {
		return lst.stream().skip(size * page).limit(size).collect(Collectors.toList())
				.toArray(new ElementEntity[lst.size()]);
	}

	public void updateElementsInDatabase(ArrayList<ElementEntity> elements, String creatorPlayground,
			String userPlayground, String email) {
		try {
			for (ElementEntity el : elements) {
				updateElementInDatabaseFromExternalElement(el, userPlayground, email);
			}

		} catch (ElementDataException e) {
			throw new ElementDataException("element in collection have fields that are incorrect");
		}

	}

	@Override
	public void updateElementInDatabaseFromExternalElement(ElementEntity element, String userPlayground, String email) {
		userService.login(userPlayground, email);
		ElementEntity tempElement = this.getElement(element.getSuperkey(), userPlayground, email);
		if (tempElement != null) {
			elements.remove(tempElement);
			elements.add(element);
		} else
			throw new ElementDataException("element data for update is incorrect");
	}
	
	@Override
	public void replaceElementWith(ElementEntity entity, String id, String creatorPlayground, String userPlayground,
			String email) {
		userService.login(userPlayground, email);
		ElementEntity tempElement = this.getElement(ElementEntity.setSuperkey(id, creatorPlayground), userPlayground, email);
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
	public ElementEntity[] getAllElementsInRadius(ElementEntity element, double x, double y, double distance, int page,
			int size) {

		if (distance < 0) {
			throw new RuntimeException("Negative distance (" + distance + ")");
		}
		ArrayList<ElementEntity> lst = new ArrayList<>();
		for (ElementEntity el : elements) {
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
	public void printElementDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addElementNoLogin(ElementEntity element) {
		elements.add(element);
	}

	@Override
	public void updateElementsInDatabase(ArrayList<ElementEntity> elements, String userPlayground, String email) {
		// TODO Auto-generated method stub
		
	}







}
