package playground.logic;

import java.util.ArrayList;

public interface ElementService {

	void cleanElementService();

	ElementEntity[] getAllElementsTOInRadius(ElementEntity element, double x, double y, double distance, int page,
			int size);

	public void addElements(ElementEntity[] elements, String userPlayground);

	void updateElementInDatabaseFromExternalElement(ElementEntity element, String userPlayground, String playground,
			String id);

	ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail, String attributeName,
			String value, int page, int size);

	boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground, String creatorEmail);

	ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page, int size);

	ElementEntity getElement(String id, String playground);

	void addElement(ElementEntity element);

	ArrayList<ElementEntity> getElements();

	ElementEntity[] getElementsBySizeAndPage(ArrayList<ElementEntity> lst, int page, int size);

	void updateElementsInDatabase(ArrayList<ElementEntity> elements, String playground);
}
