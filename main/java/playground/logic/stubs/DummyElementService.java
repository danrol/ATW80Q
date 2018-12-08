package playground.logic.stubs;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import playground.exceptions.ElementDataException;
import playground.layout.ElementTO;
import playground.logic.ElementEntity;
import playground.logic.ElementService;

//@Service
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
	public void addElements(ElementEntity[] elements,String  Playground) {

		for (int i=0;i<elements.length;i++)
		{
			addElement(elements[i]);
		}


	}

	@Override
	public ElementEntity getElement(String id, String playground) {
		for (ElementEntity e : elements) {
			if (e.getId().equals(id) && e.getPlayground().equals(playground))
				return e;
		}
		throw new RuntimeException("Could not find specified element (id=" + id +") in " + playground);
	}

	
	@Override
	public ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page, int size) {
		ArrayList<ElementEntity> result = new ArrayList<>();
		for (ElementEntity element : elements) {
			if (checkEmailAndPlaygroundInElement(element, creatorPlayground, email))
				result.add(element);
		}
		return getElementsBySizeAndPage(result, page, size);
	}

	@Override
	public boolean checkEmailAndPlaygroundInElement(ElementEntity element,
			String creatorPlayground, String creatorEmail) {
		if (element.getCreatorPlayground() == creatorPlayground && element.getCreatorEmail() == creatorEmail)
			return true;
		else
			return false;
	}

	@Override
	public ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail,
			String attributeName, String value, int page, int size) {
		// TODO Auto-generated method stub
		ArrayList<ElementEntity> tempElementsList = new ArrayList<>();
		System.out.println("Entered get elements with value in attr");
		for (ElementEntity element : elements) {
			if (element.getCreatorPlayground().equals(creatorPlayground) && 
					element.getCreatorEmail().equals(creatorEmail)
					&& element.getAttributes().containsKey(attributeName)
					&& element.getAttributes().get(attributeName).equals(value))
				tempElementsList.add(element);
		}
		if (tempElementsList.isEmpty())return new ElementEntity[0];
		else return getElementsBySizeAndPage(tempElementsList, page, size);
	}

	//return arrays values depending on page and size
	@Override
	public ElementEntity[] getElementsBySizeAndPage(ArrayList<ElementEntity> lst, int page, int size) {  
		return lst
				.stream()
				.skip(size * page) 
				.limit(size) 
				.collect(Collectors.toList()) 
				.toArray(new ElementEntity[lst.size()]);
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
	public boolean isElementInDatabase(ElementEntity element) {
		if(elements.contains(element)){
			return true;
		}
		else {
			return false;
		}
		
	}

	@Override
	public ElementEntity[] getAllElementsInRadius(ElementEntity element, 
			double x, double y, double distance, int page, int size) {

		if(distance<0) {
			throw new RuntimeException("Negative distance (" + distance + ")");
		}
		ArrayList<ElementEntity> lst = new ArrayList<>();
		for (ElementEntity el : elements) {
			double xin = el.getLocation().getX() - element.getLocation().getX();
			double yin = el.getLocation().getY() - element.getLocation().getY();

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
		ElementEntity[] arr=new ElementEntity[elements.size()];
		for(int i=0;i<arr.length;i++) {
			arr[i]=elements.get(i);
		}
		
		return arr;
		
	}


}
