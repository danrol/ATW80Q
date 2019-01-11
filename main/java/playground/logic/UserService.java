package playground.logic;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;

public interface UserService {

	ArrayList<UserEntity> getUsers();

	UserEntity[] getUsers(Pageable pageable);

	UserEntity addUser(UserEntity user);

	UserEntity verifyUser(String email, String playground, String code);

	void cleanUserService();

	void updateUser(UserEntity user);

	UserEntity getUser(String playground, String email);

	UserEntity login(String playground, String email);

	void addUser(NewUserForm user);

	boolean isUserInDatabase(UserEntity user);

	void updateUser(String playground, String email, UserEntity user);

	void addPointsToUser(String user_id, long points);

	UserEntity getUser(String superkey);

	String createKey(String email, String playground);

	UserEntity createUserEntity(String json);

	ArrayList<UserEntity> getHighScoresFromHighestToLowest(Pageable pageable);

}
