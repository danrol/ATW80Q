
package playground.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import playground.aop.MyLog;
import playground.layout.ElementTO;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.ErrorException;


@RestController
public class ElementController {

	private ElementService elementService;

	@Autowired
	public void setElementService(ElementService elementService){
		this.elementService = elementService;
	}
	
	@RequestMapping(method=RequestMethod.POST,path="/playground/elements/{userPlayground}/{email}",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO SaveElement(@PathVariable("userPlayground")String userPlayground ,@PathVariable("email") String email, @RequestBody ElementTO element)  {
		// function 5
		ElementEntity t = element.toEntity();
		t = elementService.addElement(userPlayground, email, t);
		ElementTO elementT = new ElementTO(this.elementService.getElement(userPlayground,email,t.getSuperkey()));
		return elementT;
	}
	
	
	@RequestMapping(method=RequestMethod.PUT,path = "/playground/elements/{userPlayground}/{email}/{playground}/{id}",consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@PathVariable("userPlayground") String userPlayground, @PathVariable("email") String email, @RequestBody ElementTO element, @PathVariable("playground") String creatorPlayground, @PathVariable("id") String id) {
		/* function 6
		 * INPUT: ElementTO
		 * OUTPUT: NONE
		 */
		elementService.replaceElementWith(userPlayground,email,element.toEntity(),id,creatorPlayground);
	}

	@RequestMapping(method=RequestMethod.GET,path="/playground/elements/{userPlayground}/{email}/{playground}/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO getElement(@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground,@PathVariable("playground") String creatorPlayground,@PathVariable("id") String id) 
	{
		/* function 7
		 * INPUT: NONE
		 * OUTPUT: ElementTO
		 */
		ElementEntity element = elementService.getElement(userPlayground, email, id, creatorPlayground);
		return new ElementTO(element);
	}

	@RequestMapping(method=RequestMethod.GET,path="/playground/elements/{userPlayground}/{email}/all", produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getAllElements(Pageable pageable, @PathVariable("email") String email, @PathVariable("userPlayground") String userPlayground) {
		//function 8
		ArrayList<ElementEntity> allElements = elementService.getElements(pageable);
		if (allElements.size() != 0  ) 
			return getElementTOArray(elementService.lstToArray(allElements));
		else return new ElementTO[0];
		
	}
	

	@RequestMapping(method=RequestMethod.GET,path="/playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getElementsAroundLocation(Pageable pageable, @PathVariable("email") String email, @PathVariable("userPlayground") String userPlayground, @PathVariable("distance") double distance, @PathVariable("x") double x, @PathVariable("y") double y){
		//function 9
		ElementEntity[] elements = elementService.getAllElementsInRadius(userPlayground, email,x,y,distance, pageable);
		return getElementTOArray(elements);
	}
	

	@RequestMapping(method=RequestMethod.GET, path="/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getElementsByAttributeNameValue(Pageable pageable, @PathVariable("userPlayground") String userPlayground, @PathVariable ("email") String email, @PathVariable("attributeName") String attributeName, @PathVariable("value") String value) {
		/* function 10
		 * INPUT: NONE
		 * OUTPUT: ElementTO[]
		 */
		ElementEntity[] elementsWithValueInAttr= elementService.getElementsWithValueInAttribute(userPlayground, email, attributeName, value, pageable);
		if (elementsWithValueInAttr.length != 0  ) 
			return getElementTOArray(elementsWithValueInAttr);
		else return new ElementTO[0];

	}
	
	public ElementTO[] getElementTOArray(ElementEntity[] lst){ArrayList<ElementTO> result = new ArrayList<>();
		for (ElementEntity e : lst)
			result.add(new ElementTO(e));
		return result.toArray(new ElementTO[lst.length]);
	}
	
	public ElementEntity[] getElementTOArray(ElementTO[] lst){
		ArrayList<ElementEntity> result = new ArrayList<>();
		for (ElementTO e : lst) {
			if(e != null)
			result.add(e.toEntity());
		}
		return result.toArray(new ElementEntity[result.size()]);
	}
	
	@MyLog
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorException handleException(Exception e) {
		String message = e.getMessage();
		if (message == null)
			message = "There is no relevant message";
		return new ErrorException(message);
	}
}
