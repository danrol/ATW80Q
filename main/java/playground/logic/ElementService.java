package playground.logic;

import java.util.ArrayList;

import playground.layout.ElementTO;

public interface ElementService {

	void cleanElementService();

	ElementTO[] getAllElementsTOInRadius(ElementTO element, double x, double y, double distance,int page, int size);
	void updateElementInDatabaseFromExternalElement(ElementEntity element, String id, String playground);
	ElementTO[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail, String attributeName,
			String value,int page, int size);
	Boolean checkIfElementsMadeBySpecificUserInSpecificPlayground(ElementEntity element, String creatorPlayground,
			String creatorEmail);
	ElementTO[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page, int size);
	ElementEntity getElement(String id, String playground);
	void addElement(ElementEntity element);
	ArrayList<ElementEntity> getElements();
	void updateElementsInDatabase(ArrayList<ElementEntity> elements, String playground);

}
