package playground.logic;

import java.util.ArrayList;

public interface UserService {

	ArrayList<UserEntity> getUsers();

	UserEntity addUser(UserEntity user);

	UserEntity verifyUser(String email, String playground, String code);

	void cleanUserService();

	void updateUser(UserEntity user);

	UserEntity getUser(String email, String playground);

	UserEntity login(String playground, String email);

	void updateUser(UserEntity user, String email, String playground);

	void addUser(NewUserForm user);
	
	public void DBToString();

}
