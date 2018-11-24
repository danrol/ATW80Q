package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.elements.ElementTO;
import playground.exceptions.ConfirmException;
import playground.logic.ActivityTO;

@RestController
public class EliaController {

	@RequestMapping(
			method=RequestMethod.GET,
			path="elia_check",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String viewMessages() {
		return "Elia";
	}
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distan ce}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getElementsAtLocation(@RequestBody ElementTO element,double distance)throws ConfirmException{
		if(distance<1) {
			throw new ConfirmException("distance is not in the natural numbers");
		}
		if(element.getCreatorPlayground().equals(" ")) {
			throw new ConfirmException("element->CreatorPlayground :is not valid");
		}
		ElementTO[] obelement= db.getAllElementsInRadius(element,distance);
		
		
		return obelement;
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/elements/{userPlayground }/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
    public String setUser (@RequestBody ElementTO element)  {
		db.addElement(element);
		return " ";
		
		
	}
}
