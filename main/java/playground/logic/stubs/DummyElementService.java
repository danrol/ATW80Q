package playground.logic.stubs;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import playground.Constants;
import playground.exceptions.ElementDataException;
import playground.layout.ElementTO;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.Location;

@Service
public class DummyElementService implements ElementService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ArrayList<ElementEntity> elements = new ArrayList<ElementEntity>();

	@Override
	public ArrayList<ElementEntity> getElements() {
		return elements;
	}

	public static void setElements(ArrayList<ElementEntity> elementsToSet) {
		elements = elementsToSet;
	}

	@Override
	public void addElement(ElementEntity element) {
		elements.add(element);
	}
	
	@Override
	public void addElements(ElementTO[] elements,String  userPlayground) {
		ArrayList<ElementEntity> arr=new ArrayList<ElementEntity>();
		for (int i=0;i<elements.length;i++)
		{
			arr.add(elements[i].toEntity());
		}
		updateElementsInDatabase(arr, userPlayground);
		
	}

	@Override
	public ElementEntity getElement(String id, String playground) {
		for (ElementEntity e : elements) {
			if (e.getId().equals(id) && e.getPlayground().equals(playground))
				return e;
		}
		return null;
	}

	@Override
	public ElementTO[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page, int size) {
		ArrayList<ElementTO> result = new ArrayList<>();
		for (ElementEntity element : elements) {
			if (checkIfElementsMadeBySpecificUserInSpecificPlayground(element, creatorPlayground, email))
				result.add(new ElementTO(element));
		}
		return getElementsBySizeAndPage(result, page, size);
	}

	@Override
	public Boolean checkIfElementsMadeBySpecificUserInSpecificPlayground(ElementEntity element,
			String creatorPlayground, String creatorEmail) {
		if (element.getCreatorPlayground() == creatorPlayground && element.getCreatorEmail() == creatorEmail)
			return true;
		else
			return false;
	}

	@Override
	public ElementTO[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail,
			String attributeName, String value, int page, int size) {
		// TODO Auto-generated method stub
		ArrayList<ElementTO> tempElementsList = new ArrayList<>();
		for (ElementEntity element : elements) {
			if (checkIfElementsMadeBySpecificUserInSpecificPlayground(element, creatorPlayground,
					creatorEmail) && element.getAttributes().containsKey(attributeName)
					&& element.getAttributes().get(attributeName).equals(value))
				tempElementsList.add(new ElementTO(element));
		}
		return getElementsBySizeAndPage(tempElementsList, page, size);
	}
	
	//return arrays values depending on page and size
	public ElementTO[] getElementsBySizeAndPage(ArrayList<ElementTO> lst, int page, int size) {  
		 return lst
		.stream()
		.skip(size * page) 
		.limit(size) 
		.collect(Collectors.toList()) 
		.toArray(new ElementTO[lst.size()]);
	}

	public void updateElementsInDatabase(ArrayList<ElementEntity> elements, String playground) {
		try {
			for (ElementEntity el : elements) {
				updateElementInDatabaseFromExternalElement(el, el.getCreatorPlayground(), playground, el.getId());
			}

		} catch (ElementDataException e) {
			throw new ElementDataException("element in collection have fields that are incorrect");
		}

	}

	@Override
	public void updateElementInDatabaseFromExternalElement(ElementEntity element, String userPlayground, 
			String playground, String id) {

		System.out.println("Perform update");
		System.out.println("Not updated element" + this.getElement(id, playground));
		System.out.println("Updated element" + element);
		ElementEntity tempElement = this.getElement(id, playground);
		if (tempElement != null) {
			System.out.println("Elemnt by id and string" + this.getElement(id, playground).toString());

			System.out.println("temp element" + tempElement.toString());
			System.out.println("element" + element.toString());
			elements.remove(tempElement);
			elements.add(element);
		} else
			throw new ElementDataException("element data for update is incorrect");
	}

	@Override
	public ElementTO[] getAllElementsTOInRadius(ElementTO element, 
			double x, double y, double distance, int page, int size) {
		
		if(distance<0) {
			throw new RuntimeException("Negative distance (" + distance + ")");
		}
		ArrayList<ElementTO> array = new ArrayList<>();
		for (ElementEntity el : elements) {
			double xin = el.getLocation().getX() - element.getLocation().getX();
			double yin = el.getLocation().getY() - element.getLocation().getY();

			if (Math.sqrt(xin * xin + yin * yin) <= distance) {
				array.add(new ElementTO(el));
			}
		}
		if (array.isEmpty()) {
			return null;
		} else {
			return getElementsBySizeAndPage(array, page, size);
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
	public boolean equalsEntity(ElementEntity e1, ElementEntity e2) {
		if(e1.getName().equals(e2.getName())) {
			if(e1.getId().equals(e2.getId())) {
				if(e1.getType().equals(e2.getType())) {
					if(e1.getPlayground().equals(e2.getPlayground())) {
						return true;
					}
				}
			}
		}
		return false;
		
	}

	@Override
	public boolean equalsTO(ElementTO t1, ElementTO t2) {
		return equalsEntity(t1.toEntity(),t2.toEntity());
	}

	// TODO change
//	public boolean checkElementIsCorrect(ElementEntity element) {
//		UserEntity userToCheckWith = null;
//		for (UserEntity u : users) {
//			if(u.getEmail() == element.getCreatorEmail()) {
//				userToCheckWith = u;
//				break;
//			}
//		}
//		if (userToCheckWith != null && 
//				userToCheckWith.getPlayground().equals(element.getCreatorPlayground())) 
//			return true;
//		else
//			return false;
//	}

}
