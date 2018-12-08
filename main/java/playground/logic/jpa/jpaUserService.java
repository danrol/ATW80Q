package playground.logic.jpa;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import playground.dal.UserDao;
import playground.logic.NewUserForm;
import playground.logic.UserEntity;
import playground.logic.UserService;

//elia:
// to switch the service we need firstly to go to DummyUserService and remove the @Service there
@Service
public class jpaUserService implements UserService {
	
	//this is the database we need are saving in
	private UserDao userDB;
	
	@Autowired
	public jpaUserService(UserDao userDB){
		this.userDB=userDB;
	}

	@Override
	public ArrayList<UserEntity> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUser(UserEntity user) {
		if(userDB.existsById(user.getSuperkey())) {
			
		}else {
			userDB.save(user);
		}
		
	}

	@Override
	public UserEntity verifyUser(String email, String playground, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanUserService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUser(UserEntity user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserEntity getUser(String email, String playground) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserEntity login(String playground, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUser(UserEntity user, String email, String playground) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUser(NewUserForm user) {
			
	}
	

}
