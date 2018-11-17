package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.activities.ActivityTO;
import playground.database.Database;

@RestController
public class EliaController {
	@Autowired
	Database db;

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
	public String[] getElementsAtLocation() {
		return db.getElements();
		//function to do 
		int num=5;
		String[] elements;
		return  elements=new String[num];
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/elements/{userPlayground }/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
    public String setUser (@RequestBody ElementTO element)  {
		//function to do 
		return " ";
		
		
	}
}
