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
		ArrayList<ElementEntity> lst = new ArrayList<>();
		for (ElementEntity el : elementsDB.findAll()) {
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
			elementsDB.save(elements[i]);
		}
		
		
	}

	@Override
	public void updateElementInDatabaseFromExternalElement(ElementEntity element, String userPlayground,
			String playground, String id) {
		
		if(elementsDB.existsById(id+","+userPlayground)) {
			elementsDB.deleteById(id+","+userPlayground);
			elementsDB.save(element);
		}
		 
		
	}

	@Override
	@Transactional
	public ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail,
			String attributeName, String value, int page, int size) {
		ArrayList<ElementEntity> arr=(ArrayList<ElementEntity>) elementsDB.findAll();
		ArrayList<ElementEntity> arrReturned=new ArrayList<ElementEntity>();
		for(ElementEntity element:arr) {
			if(element.getCreatorPlayground().equals(creatorPlayground) && 
					element.getCreatorEmail().equals(creatorEmail)
					&& element.getAttributes().containsKey(attributeName)
					&& element.getAttributes().get(attributeName).equals(value)) {
				Optional<ElementEntity> el=elementsDB.findById(element.getSuperkey());
				if(el.isPresent()) {
					arrReturned.add(el.get());
				}
			}
		}
		return  arrReturned.toArray(new ElementEntity[arrReturned.size()]);
		
	}

	@Override
	public boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground,
			String creatorEmail) {
		Optional<ElementEntity> el=elementsDB.findById(element.getSuperkey());
		if(el.isPresent()) {
			ElementEntity elementI=el.get();
			if(elementI.getCreatorEmail().equals(creatorEmail)&&elementI.getCreatorPlayground().equals(creatorPlayground)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	@Transactional
	public ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page,
			int size) {
		ArrayList<ElementEntity> arr=(ArrayList<ElementEntity>) elementsDB.findAll();
		ArrayList<ElementEntity> arrReturned=new ArrayList<ElementEntity>();
		for(ElementEntity el:arr) {
			if(el.getCreatorEmail().equals(email)&&el.getCreatorPlayground().equals(creatorPlayground)) {
				arrReturned.add(el);
			}
		}
		return  arrReturned.toArray(new ElementEntity[arrReturned.size()]);
	}

	@Override
	@Transactional
	public ElementEntity getElement(String id, String playground) {
		Optional<ElementEntity> el=elementsDB.findById(id+","+playground);
		if(el.isPresent()) {
			ElementEntity elementI=el.get();
			if(elementI.getCreatorPlayground().equals(playground)) {
				return elementI;
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
			elementsDB.save(element);
		}
		
		
	}

	@Override
	@Transactional
	public ArrayList<ElementEntity> getElements() {
		
		Iterator<ElementEntity> iter = (Iterator<ElementEntity>) elementsDB.findAll();
		ArrayList<ElementEntity> copy = new ArrayList<ElementEntity>();
		while (iter.hasNext())
		    copy.add(iter.next());
		
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
		for(ElementEntity el:elements) {
			if(elementsDB.existsById(el.getSuperkey())) {
				elementsDB.deleteById(el.getSuperkey());
				elementsDB.save(el);
			}else
			{
				elementsDB.save(el);
			}
		}
		
	}

	@Override
	@Transactional
	public ElementEntity[] getAllElements() {
		ArrayList<ElementEntity> arr=getElements();
		return arr.toArray(new ElementEntity[arr.size()]);
	}

}
