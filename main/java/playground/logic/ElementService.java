package playground.logic;

import java.util.ArrayList;
import org.springframework.data.domain.Pageable;


public interface ElementService {

	void cleanElementService();

	boolean isElementInDatabase(ElementEntity element);

	ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail, String attributeName, String value, Pageable pageable);

	boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground, String creatorEmail);

	ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, Pageable pageable);

	ElementEntity addElementNoLogin(ElementEntity element);

	ArrayList<ElementEntity> getElements();
	
	ArrayList<ElementEntity> getElements(Pageable pageable);

	ElementEntity[] lstToArray(ArrayList<ElementEntity> lst);

	ElementEntity[] getAllElements();

	void addElementsNoLogin(ElementEntity[] elements);

	ElementEntity getElementNoLogin(String superkey);

	double distanceBetween(double x1, double y1, double x2, double y2);

	void updateElementInDatabaseFromExternalElement(String userPlayground, String email, ElementEntity element);

	void replaceElementWith(String userPlayground, String email, ElementEntity entity, String id, String creatorPlayground);

	void updateElementsInDatabase(String userPlayground, String email, ArrayList<ElementEntity> elements);

	ElementEntity getElement(String userPlayground, String email, String superkey);

	ElementEntity getElement(String userPlayground, String email, String id, String creatorPlayground);

	void addElements(String userPlayground, String email, ElementEntity[] elements);

	ElementEntity[] getAllElementsInRadius(String userPlayground, String email, double x, double y, double distance, Pageable pageable);

	ElementEntity addElement(String userPlayground, String email, ElementEntity element);
	
	String createKey(String id, String creatorPlayground);

	void updateElementInDatabaseFromExternalElementNoLogin(ElementEntity messageBoard);

	ElementEntity createElementEntity(String json);



	
}
