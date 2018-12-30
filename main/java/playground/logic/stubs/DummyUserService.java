package playground.logic.stubs;
import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import playground.Constants;
import playground.exceptions.PermissionUserException;
import playground.exceptions.ConfirmException;
import playground.exceptions.LoginException;
import playground.exceptions.RegisterNewUserException;
import playground.logic.NewUserForm;
import playground.logic.UserEntity;
import playground.logic.UserService;

//@Service
public class DummyUserService implements UserService{
	
	private static final long serialVersionUID = 1L;
	private static ArrayList<UserEntity> users;

	public DummyUserService()
	{
		users = new ArrayList<UserEntity>();
	}


	@Override
	public ArrayList<UserEntity> getUsers() {
		return users;
	}

	public static void setUsers(ArrayList<UserEntity> usersToSet) {
		users = usersToSet;
	}
	
	@Override
	public UserEntity addUser(UserEntity user) {
		UserEntity result = new UserEntity();
		if (this.getUser(user.getEmail(), user.getPlayground()) != null)
			throw new RegisterNewUserException("User already registered");
		else {
		System.err.println("added " + user.getEmail() + " playground: " + user.getPlayground());
		users.add(user);
		result = getUser(user.getEmail(), user.getPlayground());
		}
		return result;
	}
	
	@Override
	public void addUser(NewUserForm user) {
		if (this.getUser(user.getEmail(), Constants.PLAYGROUND_NAME) != null)
			throw new RegisterNewUserException("User already registered");
		else {
				users.add(new UserEntity(user));
		}
	}
	
	
	@Override
	public UserEntity getUser(String email, String playground) {
		for(UserEntity u:users)
		{
			if(u.getEmail().equals(email) && u.getPlayground().equals(playground))
				return u;
		}
		return null;
	}
	
	@Override
	public void updateUser(UserEntity user) {
		UserEntity oldUser = this.getUser(user.getEmail(), user.getPlayground());
		if(oldUser.isVerified())
			user.verifyUser();
		String id = oldUser.getId();
		users.remove(oldUser);
		user.setId(id);
		users.add(user);
	}
	
	@Override
	public void cleanUserService() {
		users.clear();
	}
	
	@Override
	public UserEntity login(String playground, String email) {
		UserEntity u = getUser(email, playground);
		if (u != null) {
			if (u.getPlayground().equals(playground)) {
				if (u.isVerified()) {
					return u;
				} else {
					throw new LoginException("User is not verified.");
				}
			} else {
				throw new LoginException("User does not belong to the specified playground.");
			}

		} else {
			throw new LoginException("Email is not registered.");
		}
	}
	
	@Override
	public void updateUser(String playground,String email,UserEntity user) {
		
		login(playground, email);
		if(user.getSuperkey().equals(UserEntity.createKey(email, playground))) {
			updateUser(user);
		}
		else {
			throw new RuntimeException("Update user data allowed only on yourself");
		}
	}
	
	@Override
	public UserEntity verifyUser(String email, String playground, String code) {
		UserEntity user = getUser(email, playground);
		
		if(user !=null) {
			//TODO remove if. added playground check to getUser 
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

	

	@Override
	public boolean isUserInDatabase(UserEntity user) {
		
		return users.contains(user);
	}


	@Override
	public UserEntity[] getUsers(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void addPointsToUser(String user_id, long points) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public UserEntity getUser(String superkey) {
		// TODO Auto-generated method stub
		return null;
	}



}
