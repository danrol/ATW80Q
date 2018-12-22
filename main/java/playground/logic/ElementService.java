package playground.logic;

import java.util.ArrayList;

public interface ElementService {

	void cleanElementService();

	boolean isElementInDatabase(ElementEntity element);

	ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail, String attributeName,
			String value, int page, int size);

	boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground, String creatorEmail);

	ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page, int size);

	void addElementNoLogin(ElementEntity element);

	ArrayList<ElementEntity> getElements();

	ElementEntity[] getElementsBySizeAndPage(ArrayList<ElementEntity> lst, int page, int size);

	ElementEntity[] getAllElements();

	void addElementsNoLogin(ElementEntity[] elements);

	ElementEntity getElementNoLogin(String superkey);

	double distanceBetween(double x1, double y1, double x2, double y2);

	void updateElementInDatabaseFromExternalElement(String userPlayground, String email, ElementEntity element,
			ElementEntity stub);

	void replaceElementWith(String userPlayground, String email, ElementEntity entity, String id,
			String creatorPlayground, ElementEntity stub);

	void updateElementsInDatabase(String userPlayground, String email, ArrayList<ElementEntity> elements,
			ElementEntity stub);

	ElementEntity getElement(String userPlayground, String email, String superkey, ElementEntity stub);

	ElementEntity getElement(String userPlayground, String email, String id, String creatorPlayground,
			ElementEntity stub);

	void addElements(String userPlayground, String email, ElementEntity[] elements, ElementEntity stub);

	ElementEntity[] getAllElementsInRadius(String userPlayground, String email, double x, double y, double distance,
			int page, int size, ElementEntity stub);

	void addElement(String userPlayground, String email, ElementEntity element, ElementEntity stub);

}
