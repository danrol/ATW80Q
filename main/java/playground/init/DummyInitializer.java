package playground.init;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import playground.Constants;
import playground.exceptions.RegisterNewUserException;
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
		// empty
	}

	@PostConstruct
	public void init() {
		ElementEntity msgBoard = new ElementEntity("DummyMessageBoard",
				Constants.PLAYGROUND_MAIL, 0, 0);

			msgBoard = elementService.addElementNoLogin(msgBoard);

		UserEntity mod = new UserEntity("manager1", Constants.PLAYGROUND_MAIL + ".jp", "avatar",
				Constants.MANAGER_ROLE, Constants.PLAYGROUND_NAME);
		mod.verifyUser();
		try {
			mod = userService.addUser(mod);
		}
		catch(RegisterNewUserException e) {
			mod = userService.getUser(mod.getPlayground(), mod.getEmail());
		}
		String msg = "msg";
		for (int i = 0; i < 30; i++) {
			ActivityEntity entity = new ActivityEntity();
			entity.setType(Constants.MESSAGE_ACTIVITY);
			entity.getAttribute().put(Constants.ACTIVITY_MESSAGE_KEY, msg + i);
			entity.setElementId(msgBoard.getSuperkey());
			activityService.executeActivity(mod.getPlayground(),mod.getEmail(),entity,null);
			// TODO remove the comments from above - this code makes it crush for some
			// reason
		}

	}

}
