package playground.logic;

import java.util.ArrayList;

import playground.layout.ElementTO;

public interface ElementService {

	void cleanElementService();

	ElementEntity[] getAllElementsInRadius(ElementEntity element, double x, double y, double distance, int page,
			int size);

	boolean isElementInDatabase(ElementEntity element);

	void addElements(ElementEntity[] elements, String userPlayground, String email);

	ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail, String attributeName,
			String value, int page, int size);

	boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground, String creatorEmail);

	ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page, int size);

	ElementEntity getElement(String id, String playground, String userPlayground, String email);

	void addElement(ElementEntity element, String userPlayground, String email);

	void addElementNoLogin(ElementEntity element);

	ArrayList<ElementEntity> getElements();

	ElementEntity[] getElementsBySizeAndPage(ArrayList<ElementEntity> lst, int page, int size);

	ElementEntity[] getAllElements();

	void printElementDB();

	ElementEntity getElement(String superkeyd, String userPlayground, String email);

	void addElementsNoLogin(ElementEntity[] elements);

	ElementEntity getElementNoLogin(String superkey);

	void updateElementsInDatabase(ArrayList<ElementEntity> elements, String userPlayground, String email);

	void updateElementInDatabaseFromExternalElement(ElementEntity element, String userPlayground, String email);

	void replaceElementWith(ElementEntity entity, String id, String creatorPlayground,
			String userPlayground, String email);
	
	
}
