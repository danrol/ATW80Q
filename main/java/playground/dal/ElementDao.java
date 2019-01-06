package playground.dal;

import java.util.ArrayList;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import playground.logic.ElementEntity;

@RepositoryRestResource
public interface ElementDao extends PagingAndSortingRepository<ElementEntity,String>{

	
	
	public ArrayList<ElementEntity> findAllByCreatorPlaygroundAndCreatorEmail( 
			@Param("creatorPlayground") String creatorPlayground, 
			@Param("creatorEmail") String creatorEmail, 
			Pageable pageable);
			//	ArrayList<ElementEntity> result = new ArrayList<>();
			//	for (ElementEntity element : elements) {
			//		if (checkEmailAndPlaygroundInElement(element, creatorPlayground, email))
			//			result.add(element);
			//	}	
	
	
	
	

	public ArrayList<ElementEntity> findAllByXBetweenAndYBetween(
    		@Param("x1") double d,
    		@Param("x2") double e,
    		@Param("y1") double f,
    		@Param("y2") double g,
    		Pageable pageable);
	
	
	
	
	
	

			//	public ElementEntity[] getElementsWithValueInAttribute(String userPlayground, String email, String attributeName, String value, Pageable pageable) {
			//		ArrayList<ElementEntity> elements = getElements();
			//		ArrayList<ElementEntity> tempElementsList = new ArrayList<>();
			//		for (ElementEntity e : elements) {
			//			if (e.getAttributes().containsKey(attributeName) && e.getAttributes().get(attributeName).equals(value))
			//				if(roleIsCorrectExpirationDateCheck(userService.getUser(userPlayground, email), e.getExpirationDate()))
			//					tempElementsList.add(e);
			//		}
			//		if (tempElementsList.isEmpty()) 
			//			return new ElementEntity[0];
			//		 else
			//			return getElementsBySizeAndPage(tempElementsList, pageable);
			//
			//	}
	
	
	
	
			//	public ElementEntity getElementNoLogin(String superkey) {
			//		Optional<ElementEntity> el = elementsDB.findById(superkey);
			//		if (el.isPresent()) {
			//				ElementEntity t = el.get();
			//				return t;
			//		}
			//		else
			//			throw new ElementDataException("Could not find element " + superkey);
			//		
			//	}

	
	
	
	

			//	public ArrayList<ElementEntity> getElements() {
			//		ArrayList<ElementEntity> lst = new ArrayList<ElementEntity>();
			//		for (ElementEntity e : elementsDB.findAll())
			//			lst.add(e);
			//		System.err.println("lst: " + lst);
			//		return lst;
			//	}
	
	
	
	
			//	public ArrayList<ElementEntity> getElements(Pageable pageable) {
			//		ArrayList<ElementEntity> lst = new ArrayList<ElementEntity>();
			//		for (ElementEntity e : elementsDB.findAll(pageable))
			//			lst.add(e);
			//		return lst;
			//	}
	
	
			//	public ElementEntity[] getAllElements() {
			//		ArrayList<ElementEntity> arr = getElements();
			//		return arr.toArray(new ElementEntity[arr.size()]);
			//	}
	
				
			//	@Query(value = "from ELEMENT s join s.attributes a where a.name = ?1 AND a.value= ?1")
			//	public ArrayList<ElementEntity> findAllByAttributNameAndAttributeValue(
			//			String attributeName,
			//			Object attributeValue,
			//			Pageable pageable);
	
}
	
