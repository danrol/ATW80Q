package playground.logic.jpa;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import playground.logic.UserEntity;
import playground.logic.UserService;

//@Service
public class jpaUserService implements UserService{

	@Override
	public ArrayList<UserEntity> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUser(UserEntity user) {
		// TODO Auto-generated method stub
		
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
	public UserEntity getUser(String email) {
		// TODO Auto-generated method stub
		return null;
	}

}
