package playground.logic.jpa;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import playground.dal.ActivityDao;
import playground.logic.ActivityService;
import playground.logic.ElementService;
import playground.logic.UserService;

@Service
public class JpaActivityService implements ActivityService {
	private ActivityDao activityDao;
	private IdGeneratorDao idGenerator;
	private ElementService elementService;
	private UserService userService;
	
	@Autowired
	public JpaActivityService(ActivityDao activity, IdGeneratorDao idGenerator) {
		this.activityDao = activity;
		this.idGenerator = idGenerator;
	}

	@Override
	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
		
	}

	@Override
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
		
	}

}
