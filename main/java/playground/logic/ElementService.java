package playground.logic;

import java.util.ArrayList;

import playground.layout.ElementTO;

public interface ElementService {

	void cleanElementService();

	public ElementEntity[] getAllElementsInRadius(ElementEntity element, 
			double x, double y, double distance, int page, int size);
	
	public boolean isElementInDatabase(ElementEntity element);

	public void addElements(ElementEntity[] elements, String userPlayground);

	void updateElementInDatabaseFromExternalElement(ElementEntity element, String creatorEmail, String playground);

	ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail, String attributeName,
			String value, int page, int size);

	boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground, String creatorEmail);

	ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page, int size);

	ElementEntity getElement(String id, String playground);

	void addElement(ElementEntity element);

	ArrayList<ElementEntity> getElements();

	ElementEntity[] getElementsBySizeAndPage(ArrayList<ElementEntity> lst, int page, int size);

	void updateElementsInDatabase(ArrayList<ElementEntity> elements, String playground);

	ElementEntity[] getAllElements();
	public void printElementDB() ;
}
