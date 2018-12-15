package playground.logic.stubs;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import playground.Constants;
import playground.exceptions.ChangeUserException;
import playground.exceptions.ConfirmException;
import playground.exceptions.LoginException;
import playground.exceptions.RegisterNewUserException;
import playground.logic.NewUserForm;
import playground.logic.UserEntity;
import playground.logic.UserService;

//@Service
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
		UserEntity user1 = new UserEntity("username1", "username1@gmail.com", "avatar1", "moderator", Constants.PLAYGROUND_NAME);
		UserEntity user2 = new UserEntity("username2", "username2@gmail.com", "avatar2", "player", Constants.PLAYGROUND_NAME);
		UserEntity user3 = new UserEntity("username2", "username3@gmail.com", "avatar3", "player", Constants.PLAYGROUND_NAME);
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
		UserEntity tempUser = this.getUser(user.getEmail(), user.getPlayground());
		users.remove(tempUser);
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
	public void updateUser(UserEntity user, String email,String playground) {
		
		login(playground, email);
		if (getUser(email, playground).getRole().equals(Constants.MODERATOR_ROLE)) {
			if(user.getEmail().equals(email)) {
				updateUser(user);
			}
			else if (!user.getRole().equals(Constants.MODERATOR_ROLE)) {
				updateUser(user);
			} else {
				throw new ChangeUserException("Moderator cannot change other moderator user");
			}
		} else if (getUser(email, playground).getRole().equals(Constants.PLAYER_ROLE)) {
			if (email.equals(user.getEmail())) {
				updateUser(user);
			} else {
				throw new ChangeUserException("PLAYER_ROLE cannot change other users information");
			}
		} else {
			throw new ChangeUserException("invalid role " + getUser(email, playground).getRole());
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
	public void printUserDB() {
		// TODO Auto-generated method stub
		
	}



}
