package playground.init;


import javax.annotation.PostConstruct;
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
	
	@Autowired
	public DummyInitializer(ElementService elementService, ActivityService activityService, UserService userService) {
		this();
		this.elementService = elementService;
		this.activityService = activityService;
		this.userService = userService;
	}
	
	public DummyInitializer() {
		//empty
	}

	@PostConstruct
	public void init() {
		ElementEntity msgBoard = new ElementEntity("DummyMessageBoard", Constants.PLAYGROUND_NAME, Constants.PLAYGROUND_MAIL, 0, 0);
		msgBoard = elementService.addElementNoLogin(msgBoard);
		UserEntity mod = new UserEntity("moderator1", Constants.PLAYGROUND_MAIL+".jp", "avatar", Constants.MODERATOR_ROLE, Constants.PLAYGROUND_NAME);
		mod.verifyUser();
		mod=userService.addUser(mod);
		String msg = "msg";
		/*
		for(int i=0;i<3;i++)
		{
		
			ActivityEntity entity = new ActivityEntity();
			entity.setType(Constants.MESSAGE);
			entity.getAttribute().put(Constants.MESSAGE_ATTR, msg + i);
			entity.getAttribute().put(Constants.MESSAGEBOARD_ID_KEY, msgBoard.getSuperkey());
			activityService.executeActivity(mod.getPlayground(),mod.getEmail(),entity);
			
			
			//TODO remove the comments from above - this code makes it crush for some reason
		}
		*/
	}
	
}
