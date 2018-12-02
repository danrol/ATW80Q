package playground.logic;

import java.util.ArrayList;
import playground.layout.UserTO;

public interface UserService {

	ArrayList<UserEntity> getUsers();
	void addUser(UserEntity user);
	UserEntity verifyUser(String email, String playground, String code);
	void cleanUserService();
	void updateUser(UserEntity user);
	UserEntity getUser(String email);
	Boolean CheckIfUserLoggedIn(UserEntity userToCheck);
	UserTO login(String playground, String email);
	void updateUser(UserEntity user, String email, String playground);
	public boolean equalsEntity(UserEntity e1,UserEntity e2);
	public boolean equalsTO(UserTO t1,UserTO t2);

}
