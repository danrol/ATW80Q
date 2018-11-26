package playground.logic;

import java.util.ArrayList;

public interface UserService {

	ArrayList<UserEntity> getUsers();
	void addUser(UserEntity user);
	UserEntity verifyUser(String email, String playground, String code);
	void cleanUserService();
	void updateUser(UserEntity user);
	UserEntity getUser(String email);

}
