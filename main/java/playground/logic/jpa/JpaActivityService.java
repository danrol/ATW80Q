package playground.logic.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import playground.dal.ActivityDao;
import playground.logic.ActivityService;
import playground.logic.ElementService;
import playground.logic.UserService;

@Service
public class JpaActivityService implements ActivityService {
	private ActivityDao activity;
	private IdGeneratorDao idGenerator;
	
	@Autowired
	public JpaMessageService(ActivityDao activity, IdGeneratorDao idGenerator) {
		this.activity = activity;
		this.idGenerator = idGenerator;
	}

	@Override
	public void setElementService(ElementService elementService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserService(UserService userService) {
		// TODO Auto-generated method stub
		
	}

}
