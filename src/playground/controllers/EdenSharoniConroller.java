package playground.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EdenSharoniConroller {

	@RequestMapping(
			method=RequestMethod.GET,
			path="/eden_sharoni_check",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String viewMessages() {
		return "eden sharoni";
	}
}
