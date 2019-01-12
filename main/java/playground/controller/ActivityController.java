package playground.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import playground.aop.MyLog;
import playground.constants.Playground;
import playground.layout.ActivityTO;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ErrorException;

@RestController
public class ActivityController {

	private ActivityService activityService;

	@Autowired
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}

	@RequestMapping
	(method = RequestMethod.POST, 
	path = "/playground/activities/{userPlayground}/{email}", 
	consumes = MediaType.APPLICATION_JSON_VALUE, 
	produces = MediaType.APPLICATION_JSON_VALUE)
	public Object RequestServer(@PathVariable("userPlayground") String userPlayground, @PathVariable("email") String email, @RequestBody ActivityTO activity,Pageable pageable) {
		/*
		 * function 11 INPUT: ActivityTO OUTPUT: Object
		 */
		
		ActivityEntity activityEnt = activity.toEntity();
		activityEnt.setPlayerEmail(email);
		activityEnt.setPlayerPlayground(userPlayground);
		Object t = activityService.executeActivity(userPlayground, email,activityEnt ,pageable);
		return t;
	}
	
	@MyLog
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorException handleException(Exception e) {
		String message = e.getMessage();
		if (message == null)
			message = Playground.NO_RELEVANT_MESSAGE_ERROR;
		return new ErrorException(message);
	}


}
