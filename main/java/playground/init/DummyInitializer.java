package playground.init;


import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import playground.Constants;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;


@Component
@Profile("demo")
public class DummyInitializer {
	private ElementService elementService;
	private ActivityService activityService;
	private UserService userService;
	private Log log = LogFactory.getLog(DummyInitializer.class);
	
	@Autowired
	public DummyInitializer(ElementService elementService, ActivityService activityService, UserService userService) {
		super();
		this.elementService = elementService;
		this.activityService = activityService;
		this.userService = userService;
	}
	
	@PostConstruct
	public void init() {
		ElementEntity msgBoard = new ElementEntity("0","DummyMessageBoard", Constants.PLAYGROUND_NAME, Constants.PLAYGROUND_MAIL, 0, 0);
		UserEntity mod = new UserEntity("moderator1", Constants.PLAYGROUND_MAIL, "avatar", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		mod.verifyUser();
		userService.addUser(mod);
		String msg = "msg";
		for(int i=0;i<100;i++)
		{
			ActivityEntity entity = new ActivityEntity();
			entity.setType(Constants.MESSAGE_WRITE);
			entity.getAttribute().put(Constants.MESSAGE_ATTR, msg + i);
			entity.getAttribute().put(Constants.MESSAGEBOARD_KEY, msgBoard.getSuperkey());
			activityService.executeActivity(mod.getPlayground(),mod.getEmail(),entity);
		}
	}
	
}
