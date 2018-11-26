package playground.logic;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import playground.Constants;

@Service
public class UserService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ArrayList<UserEntity> users;

	public UserService()
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

	public static ArrayList<UserEntity> getUsers() {
		return users;
	}

	public static void setUsers(ArrayList<UserEntity> usersToSet) {
		users = usersToSet;
	}
	
	public void addUser(UserEntity user) {
		System.err.println("added " + user.getEmail() + " playground: " + user.getPlayground());
		users.add(user);
	}
	
	public synchronized UserEntity getUser(String email) {
		for(UserEntity u:users)
		{
			if(u.getEmail().equals(email))
				return u;
		}
		return null;
	}
	
	public synchronized void updateUserInDatabase(UserEntity user) {
		UserEntity tempUser = this.getUser(user.getEmail());
		users.remove(tempUser);
		users.add(user);
	}
	
	public synchronized void cleanUserService() {
		users.clear();
	}
}