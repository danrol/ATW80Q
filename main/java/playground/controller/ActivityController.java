package playground.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.aop.LoginRequired;
import playground.layout.ActivityTO;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementService;
import playground.logic.UserService;



@RestController
public class ActivityController {

	private ElementService elementService;
	private UserService userService;
	private ActivityService activityService;

	@Autowired
	public void setElementService(ElementService elementService){
		this.elementService = elementService;
	}

	@Autowired
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	@Autowired
	public void setActivityService(ActivityService activityService){
		this.activityService = activityService;
	}
	
	@LoginRequired
	@RequestMapping(method=RequestMethod.POST,path="/playground/activities/{userPlayground}/{email}",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public Object RequestServer(@PathVariable("userPlayground") String userPlayground, @PathVariable("email") String email,@RequestBody ActivityTO activity) 
	{
		/* function 11
		 * INPUT: ActivityTO
		 * OUTPUT: Object
		 */
		ActivityEntity t = activityService.addActivity(activity.toEntity());
		return new ActivityTO(t); 
	}
	
	

}
