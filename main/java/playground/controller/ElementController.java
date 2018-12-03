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

import playground.exceptions.ConfirmException;
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
	

	@RequestMapping(method=RequestMethod.POST,path="/playground/elements/{userPlayground }/{email}",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO setUser (@RequestBody ElementTO element,@PathVariable("email") String email,@PathVariable("userPlayground")String userPlayground)throws ConfirmException  {
		// function 5

		userService.login(userPlayground,email);
		elementService.addElement(element.toEntity());
		return new ElementTO(this.elementService.getElement(element.getId(),element.getPlayground()));
	}

	@RequestMapping(method=RequestMethod.PUT,path = "/playground/elements/{userPlayground}/{email}/{playground}/{id}",consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@RequestBody ElementTO element, @PathVariable("email") String email,
			@PathVariable("userPlayground") String userPlayground, @PathVariable("playground") String playground,
			@PathVariable("id") String id) {
		/* function 6
		 * INPUT: ElementTO
		 * OUTPUT: NONE
		 */

		System.out.println("Entered update");
		userService.login(userPlayground, email);
		elementService.updateElementInDatabaseFromExternalElement(element.toEntity(), userPlayground, 
				playground, id);
		System.out.println("updatePerformed");
	}

	@RequestMapping(method=RequestMethod.GET,path="/playground/elements/{userPlayground}/{email}/{playground}/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO getElement(@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground,@PathVariable("playground") String playground,@PathVariable("id") String id) 
	{
		/* function 7
		 * INPUT: NONE
		 * OUTPUT: ElementTO
		 */
		ElementEntity element = null;
		userService.login(userPlayground,email);
		//if login succeeded, get element
		element = elementService.getElement(id, playground);
		if(element == null)
			throw new RuntimeException("Could not find specified element (id=" + id +") in " + playground);

		return new ElementTO(element);
	}

	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/elements/{userPlayground }/{email}/all",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] setAllUsers (
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestBody ElementTO[] element,
			@PathVariable("email") String email,
			@PathVariable("userPlayground")String userPlayground)throws ConfirmException  {

		//function 8
		userService.login(userPlayground,email);
		elementService.addElements(element, userPlayground);
		return getElementTOArray(elementService.getElementsByCreatorPlaygroundAndEmail(userPlayground, email, page, size));	
	}


	@RequestMapping(method=RequestMethod.GET,path="/playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getElementsAtLocation(
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestBody ElementTO element,
			@PathVariable("email") String email,
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("distance") double distance, 
			@PathVariable("x") int x, 
			@PathVariable("y") int y)throws ConfirmException{
		//function 9

		userService.login(userPlayground,email);

		return getElementTOArray(elementService.getAllElementsTOInRadius(element,x,y,distance, page, size));
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
		System.out.println();
		userService.login(userPlayground, email);
		ElementEntity[] elementsWithValueInAttr= elementService.
		getElementsWithValueInAttribute(userPlayground, email, attributeName, value, page, size);
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
}
