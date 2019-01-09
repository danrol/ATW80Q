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
import playground.exceptions.ErrorException;
import playground.layout.ActivityTO;
import playground.layout.ElementTO;
import playground.layout.UserTO;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.UserEntity;

@RestController
public class ActivityController {

	private ActivityService activityService;

	@Autowired
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/playground/activities/{userPlayground}/{email}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object RequestServer(@PathVariable("userPlayground") String userPlayground, @PathVariable("email") String email, @RequestBody ActivityTO activity,Pageable pageable) {
		/*
		 * function 11 INPUT: ActivityTO OUTPUT: Object
		 */
		
		
		System.err.println(email);
		System.err.println(activity);
		System.err.println(pageable);
		System.err.println(userPlayground);
		ActivityEntity activityEnt = activity.toEntity();
		activityEnt.setPlayerEmail(email);
		activityEnt.setPlayerPlayground(userPlayground);
		System.err.println(activityEnt);
		Object t = activityService.executeActivity(userPlayground, email,activityEnt ,pageable);
		System.err.println(t);
		System.err.println(t.getClass().getName());
		switch(t.getClass().getName())
		{
		case "ActivityEntity":
			return new ActivityTO((ActivityEntity) t);
		case "ElementEntity":
			return new ElementTO((ElementEntity) t);
		case "UserEntity":
			return new UserTO((UserEntity) t);
		default:
			return t;
		}
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
