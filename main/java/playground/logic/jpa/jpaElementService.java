package playground.logic.jpa;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import playground.dal.ElementDao;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
//elia:
//to switch the service we need firstly to go to DummyElementService and remove the @Service there
//@Service
public class jpaElementService implements ElementService {
	
	//this is the database we need are saving in
	private  ElementDao elementsDB;
	
	@Autowired
	public jpaElementService(ElementDao elementsDB) {
		this.elementsDB=elementsDB;
		
	}
	
	

	@Override
	public void cleanElementService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional(readOnly=true)
	public ElementEntity[] getAllElementsInRadius(ElementEntity element, double x, double y, double distance, int page,
			int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isElementInDatabase(ElementEntity element) {
		
		return this.elementsDB.existsById(element.getId());
	}

	@Override
	public void addElements(ElementEntity[] elements, String userPlayground) {
		for(int i=0;i<elements.length;i++) {
			elementsDB.save(elements[i]);
		}
		
		
	}

	@Override
	public void updateElementInDatabaseFromExternalElement(ElementEntity element, String userPlayground,
			String playground, String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail,
			String attributeName, String value, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground,
			String creatorEmail) {
		
		return false;
	}

	@Override
	public ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page,
			int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementEntity getElement(String id, String playground) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addElement(ElementEntity element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<ElementEntity> getElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementEntity[] getElementsBySizeAndPage(ArrayList<ElementEntity> lst, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateElementsInDatabase(ArrayList<ElementEntity> elements, String playground) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ElementEntity[] getAllElements() {
		// TODO Auto-generated method stub
		return null;
	}

}
