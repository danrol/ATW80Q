package playground.logic.jpa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import playground.dal.ElementDao;
import playground.exceptions.ElementDataException;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
//elia:
//to switch the service we need firstly to go to DummyElementService and remove the @Service there
@Service
public class jpaElementService implements ElementService {
	
	//this is the database we need are saving in
	private  ElementDao elementsDB;
	
	@Autowired
	public jpaElementService(ElementDao elementsDB) {
		this.elementsDB=elementsDB;
		
	}
	

	@Override
	public void cleanElementService() {
		elementsDB.deleteAll();
		
	}

	@Override
	@Transactional
	public ElementEntity[] getAllElementsInRadius(ElementEntity element, double x, double y, double distance, int page,
			int size) {
		if(distance<0) {
			throw new RuntimeException("Negative distance (" + distance + ")");
		}
		ArrayList<ElementEntity> allElements=getElements();
		ArrayList<ElementEntity> lst = new ArrayList<>();
		for (ElementEntity el : allElements) {
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
	public boolean isElementInDatabase(ElementEntity element) {
		
		return this.elementsDB.existsById(element.getSuperkey());
	}

	@Override
	public void addElements(ElementEntity[] elements, String userPlayground) {
		for(int i=0;i<elements.length;i++) {
			addElement(elements[i]);
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
			elementsDB.deleteById(tempElement.getSuperkey());
			elementsDB.save(element);
		} else
			throw new ElementDataException("element data for update is incorrect");
		 
		
	}

	@Override
	@Transactional
	public ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail,
			String attributeName, String value, int page, int size) {
		
				ArrayList<ElementEntity> elements=getElements();
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

	@Override
	public boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground,
			String creatorEmail) {
			if (element.getCreatorPlayground().equals(creatorPlayground) && element.getCreatorEmail().equals(creatorEmail))
				return true;
			else
				return false;
	}

	@Override
	@Transactional
	public ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page,
			int size) {
		ArrayList<ElementEntity> elements= getElements();
		ArrayList<ElementEntity> result = new ArrayList<>();
		for (ElementEntity element : elements) {
			if (checkEmailAndPlaygroundInElement(element, creatorPlayground, email))
				result.add(element);
		}
		return getElementsBySizeAndPage(result, page, size);
	}
	

	@Override
	@Transactional
	public ElementEntity getElement(String id, String playground) {
		Optional<ElementEntity> el=elementsDB.findById(id+","+playground);
		if(el.isPresent()) {
			try {
				return el.get();	
			}catch (Exception e) {
				System.out.println("element:"+el.toString()+"/n feild to load from database");
			}
			
			
		}
		return null;
	}

	@Override
	public void addElement(ElementEntity element) {
		if(elementsDB.existsById(element.getSuperkey())) {
			System.out.println("allredy exist in database:"+element.toString());
		}else
		{
			try {
				elementsDB.save(element);
			}catch (Exception e) {
				System.err.println("element:"+element.toString()+"/n feild to be saves in the database");
			}
			
		}
		
		
	}

	@Override
	@Transactional
	public ArrayList<ElementEntity> getElements() {
		ArrayList<ElementEntity> copy = new ArrayList<ElementEntity>();
		try {
			Iterator<ElementEntity> iter = (Iterator<ElementEntity>) elementsDB.findAll();
			while (iter.hasNext())
			    copy.add(iter.next());
		}catch (Exception e) {
			System.out.println("elements convertion unsucessful");
		}
		return  copy;	
		
	}

	@Override
	@Transactional
	public ElementEntity[] getElementsBySizeAndPage(ArrayList<ElementEntity> lst, int page, int size) {
		return lst
				.stream()
				.skip(size * page) 
				.limit(size) 
				.collect(Collectors.toList()) 
				.toArray(new ElementEntity[lst.size()]);
				
		
	}

	@Override
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
	@Transactional
	public ElementEntity[] getAllElements() {
		ArrayList<ElementEntity> arr=getElements();
		return arr.toArray(new ElementEntity[arr.size()]);
	}
	@Override
	public void DBToString() {
		ArrayList<ElementEntity> copy = new ArrayList<ElementEntity>();
		try {
			Iterator<ElementEntity> iter = (Iterator<ElementEntity>) elementsDB.findAll();
			while (iter.hasNext())
			    copy.add(iter.next());
		}catch (Exception e) {
			
		}
		System.err.println("DB-TEST:all elements in database");
		for(ElementEntity e:copy) {
			System.out.println(e.toString());
		}
	}

}
