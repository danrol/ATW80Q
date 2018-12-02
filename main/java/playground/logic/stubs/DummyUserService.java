package playground.logic.stubs;


import java.util.ArrayList;

import org.springframework.stereotype.Service;

import playground.Constants;
import playground.exceptions.ConfirmException;
import playground.exceptions.LoginException;
import playground.exceptions.RegisterNewUserException;
import playground.logic.UserEntity;
import playground.logic.UserService;

@Service
public class DummyUserService implements UserService{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ArrayList<UserEntity> users;

	public DummyUserService()
	{
		users = new ArrayList<UserEntity>();
		addDummyDatabase();
	}

	public void addDummyDatabase()
	{
		UserEntity user1 = new UserEntity("username1", "username1@gmail.com", "avatar1", "moderator", Constants.PLAYGROUND_NAME, "");
		UserEntity user2 = new UserEntity("username2", "username2@gmail.com", "avatar2", "player", Constants.PLAYGROUND_NAME, "abc");
		UserEntity user3 = new UserEntity("username2", "username3@gmail.com", "avatar3", "player", Constants.PLAYGROUND_NAME, "blabla");
		this.addUser(user1);
		this.addUser(user2);
		this.addUser(user3);
	}

	@Override
	public ArrayList<UserEntity> getUsers() {
		return users;
	}

	public static void setUsers(ArrayList<UserEntity> usersToSet) {
		users = usersToSet;
	}
	@Override
	public void addUser(UserEntity user) {
		if (this.getUser(user.getEmail()) != null)
			throw new RegisterNewUserException("User already registered");
		else {
		System.err.println("added " + user.getEmail() + " playground: " + user.getPlayground());
		users.add(user);
		}
	}
	
	
	@Override
	public UserEntity getUser(String email) {
		for(UserEntity u:users)
		{
			if(u.getEmail().equals(email))
				return u;
		}
		return null;
	}
	@Override
	public void updateUser(UserEntity user) {
		UserEntity tempUser = this.getUser(user.getEmail());
		users.remove(tempUser);
		users.add(user);
	}
	@Override
	public void cleanUserService() {
		users.clear();
	}
	
	
	//EDEN DUPONT : THIS METHOD DOES NOT MATCH PROJECT REQUIREMENTS
	@Override
	public void login(String playground, String email) {
		for (UserEntity u : users) {
			if (u.getEmail().equals(email))
				if(!u.isVerified())
					throw new LoginException("User is not verified");
					if(!u.getPlayground().equals(playground))
						throw new LoginException("User is not registerd to the playground");
		}
		throw new LoginException("Login wasn't performed");
	}
	
	@Override
	public UserEntity verifyUser(String email, String playground, String code) {
		UserEntity user = getUser(email);
		
		if(user !=null) {
			if(user.getPlayground().equals(playground))
			{
				String VerificationCode = user.getVerificationCode();
				if (VerificationCode.equals(code))
					{
					user.verifyUser();
					}
				else
					{
						throw new ConfirmException("Invalid verification code");
					}
			}
				else
			{
					throw new ConfirmException("User: " + user.getEmail() +" does not belong to the specified playground ("+playground+")");
			}
		}
			else
			{
				throw new ConfirmException("Email is not registered.");
			}
		return user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
