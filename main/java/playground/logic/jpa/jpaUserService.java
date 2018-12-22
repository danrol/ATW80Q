package playground.logic.jpa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import playground.Constants;
import playground.aop.LoginRequired;
import playground.aop.MyLog;
import playground.dal.UserDao;
import playground.exceptions.PermissionUserException;
import playground.exceptions.ConfirmException;
import playground.exceptions.RegisterNewUserException;
import playground.logic.NewUserForm;
import playground.logic.UserEntity;
import playground.logic.UserService;

@Service
public class jpaUserService implements UserService {

	// this is the database we need are saving in
	private UserDao userDB;

	@Autowired
	public jpaUserService(UserDao userDB) {
		this.userDB = userDB;
	}
	
	@Override
	@Transactional
	@MyLog
	public void addDummyUsers() {
		for(int i=0; i<=25;i++) {
		userDB.save(new UserEntity("dan " +i, "dan"+i+"@mail.ru", "ava", Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME));
		}
	}

	@Override
	@Transactional
	@MyLog
	public ArrayList<UserEntity> getUsers() {
		ArrayList<UserEntity> lst = new ArrayList<UserEntity>();
			for(UserEntity u: userDB.findAll())
				lst.add(u);

		return lst;
	}
	
	@Override
	@Transactional
	@MyLog
	public UserEntity[] getUsers(Pageable pageable) {
		List<UserEntity> allUsers =  userDB.findAll(pageable).getContent();
		return turnListIntoArray(allUsers);
	}

	
	public UserEntity[] turnListIntoArray(List<UserEntity> lst) {
		return lst.toArray(new UserEntity[lst.size()]);
	}
	@Override
	@Transactional
	@MyLog
	public UserEntity addUser(UserEntity user) {
		UserEntity result = new UserEntity();
		if (userDB.existsById(user.getSuperkey())) {
			throw new RegisterNewUserException("User already registered");
		} else {

			userDB.save(user);
			result = userDB.findById(user.getSuperkey()).orElse(new UserEntity());
		}

		return result;
	}

	@Override
	@Transactional
	@MyLog
	public UserEntity verifyUser(String email, String playground, String code) {
		UserEntity user = getUser(email, playground);

		if (user != null) {
			if (user.getVerificationCode().equals(""))
				return user; // User already confirmed
			else if (user.getPlayground().equals(playground)) {
				String VerificationCode = user.getVerificationCode();
				if (VerificationCode.equals(code))
					user.verifyUser();
				else
					throw new ConfirmException("Invalid verification code");
			} else {
				throw new ConfirmException("User: " + user.getEmail() + " does not belong to the specified playground ("
						+ playground + ")");
			}
		} else {
			throw new ConfirmException("Email is not registered.");
		}
		return user;
	}

	@Override
	@MyLog
	public void cleanUserService() {
		userDB.deleteAll();

	}

	@Override
	@Transactional
	@MyLog
	public void updateUser(UserEntity user) {
		if (userDB.existsById(user.getSuperkey())) {
			try {
				userDB.deleteById(user.getSuperkey());
				userDB.save(user);
			} catch (Exception e) {
				System.out.println("failed to update user" + user.toString());
			}

		}

	}

	@Override
	@Transactional(readOnly=true)
	@MyLog
	public UserEntity getUser(String email, String playground) {
		String idToSearchBy = UserEntity.createKey(email, playground);
		UserEntity user = userDB.findById(idToSearchBy).orElse(null);
		return user;
	}

	@Override
	@Transactional(readOnly=true)
	@MyLog
	public UserEntity login(String playground, String email) {
	//TODO stub	
		return null;
	}


	@Override
	@Transactional
	@MyLog
	@LoginRequired
	public void updateUser(String playground, String email, UserEntity user) {
		if (getUser(email, playground).getRole().equals(Constants.MODERATOR_ROLE)) {
			if (user.getEmail().equals(email)) {
				updateUser(user);
			} else if (!user.getRole().equals(Constants.MODERATOR_ROLE)) {
				updateUser(user);
			} else {
				throw new PermissionUserException("Moderator cannot change other moderator user");
			}
		} else if (getUser(email, playground).getRole().equals(Constants.PLAYER_ROLE)) {
			if (email.equals(user.getEmail())) {
				updateUser(user);
			} else {
				throw new PermissionUserException("PLAYER_ROLE cannot change other users information");
			}
		} else {
			throw new PermissionUserException("Invalid role " + getUser(email, playground).getRole());
		}

	}

	@Override
	@Transactional
	@MyLog
	public void addUser(NewUserForm user) {
		if (this.getUser(user.getEmail(), Constants.PLAYGROUND_NAME) != null)
			throw new RegisterNewUserException("User already registered");
		else {
			UserEntity userEnt = new UserEntity(user.getUsername(), user.getEmail(), user.getAvatar(), user.getRole(),
					Constants.PLAYGROUND_NAME);
			addUser(userEnt);
		}

	}

	
	
	@Override
	@Transient
	@MyLog
	public boolean isUserInDatabase(UserEntity user) {

		return this.userDB.existsById(user.getSuperkey());
	}

}
