package playground.logic.jpa;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import playground.layout.ElementTO;
import playground.logic.ElementEntity;
import playground.logic.ElementService;

//@Service
public class jpaElementService implements ElementService {

	@Override
	public void cleanElementService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional(readOnly=true)
	public ElementTO[] getAllElementsTOInRadius(ElementTO element, double x, double y, double distance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void updateElementInDatabaseFromExternalElement(ElementEntity element, String id, String playground) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ElementTO[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail,
			String attributeName, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean checkIfElementsMadeBySpecificUserInSpecificPlayground(ElementEntity element,
			String creatorPlayground, String creatorEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ElementTO> getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email) {
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

}
