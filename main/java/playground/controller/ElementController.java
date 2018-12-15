package playground.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import playground.layout.ElementTO;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserService;


@RestController
public class ElementController {

	private ElementService elementService;
	private UserService userService;


	@Autowired
	public void setElementService(ElementService elementService){
		this.elementService = elementService;
	}

	@Autowired
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	

	@RequestMapping(method=RequestMethod.POST,path="/playground/elements/{userPlayground}/{email}",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO SaveElement(@RequestBody ElementTO element,@PathVariable("email") String email,@PathVariable("userPlayground")String userPlayground)  {
		// function 5

		elementService.addElement(element.toEntity(), userPlayground, email);
		ElementTO elementT = new ElementTO(this.elementService.getElement(element.getId(),element.getCreatorPlayground(),userPlayground,email));
		return elementT;
	}

	@RequestMapping(method=RequestMethod.PUT,path = "/playground/elements/{userPlayground}/{email}/{playground}/{id}",consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@RequestBody ElementTO element, @PathVariable("email") String email,
			@PathVariable("userPlayground") String userPlayground, @PathVariable("playground") String creatorPlayground,
			@PathVariable("id") String id) {
		/* function 6
		 * INPUT: ElementTO
		 * OUTPUT: NONE
		 */

		elementService.replaceElementWith(element.toEntity(),id,creatorPlayground,userPlayground,email);
	}

	@RequestMapping(method=RequestMethod.GET,path="/playground/elements/{userPlayground}/{email}/{playground}/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO getElement(@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground,@PathVariable("playground") String creatorPlayground,@PathVariable("id") String id) 
	{
		/* function 7
		 * INPUT: NONE
		 * OUTPUT: ElementTO
		 */
		ElementEntity element = null;

		element = elementService.getElement(id, creatorPlayground, userPlayground, email);

		return new ElementTO(element);
	}

	@RequestMapping(method=RequestMethod.GET,path="/playground/elements/{userPlayground}/{email}/all",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getAllElements(
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@PathVariable("email") String email,
			@PathVariable("userPlayground") String userPlayground
			) {
		//function 8


		ElementEntity[] allElements = elementService.getAllElements();
		if (allElements.length != 0  ) 
			return getElementTOArray(allElements);
		else return new ElementTO[0];
		
	}
	

	@RequestMapping(method=RequestMethod.GET,path="/playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getElementsAroundLocation(
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@PathVariable("email") String email,
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("distance") double distance, 
			@PathVariable("x") int x, 
			@PathVariable("y") int y){
		//function 9

		ElementEntity[] elements = elementService.getAllElementsInRadius(x,y,distance, page, size, userPlayground, email);
		return getElementTOArray(elements);
	}
	

	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getElementsByAttributeNameValue(
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@PathVariable("userPlayground") String userPlayground, 
			@PathVariable ("email") String email, 
			@PathVariable("attributeName") String attributeName,
			@PathVariable("value") String value) {
		/* function 10
		 * INPUT: NONE
		 * OUTPUT: ElementTO[]
		 */
		ElementEntity[] elementsWithValueInAttr= elementService.getElementsWithValueInAttribute(userPlayground, email, attributeName, value, page, size);
		if (elementsWithValueInAttr.length != 0  ) 
			return getElementTOArray(elementsWithValueInAttr);
		else return new ElementTO[0];

	}
	
	public ElementTO[] getElementTOArray(ElementEntity[] lst){
		ArrayList<ElementTO> result = new ArrayList<>();
		for (ElementEntity e : lst) {
			result.add(new ElementTO(e));
		}
		return result.toArray(new ElementTO[lst.length]);
	}
	
	public ElementEntity[] getElementTOArray(ElementTO[] lst){
		ArrayList<ElementEntity> result = new ArrayList<>();
		for (ElementTO e : lst) {
			result.add(e.toEntity());
		}
		return result.toArray(new ElementEntity[lst.length]);
	}
}
