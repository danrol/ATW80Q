package playground.logic.jpa;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import playground.Constants;
import playground.dal.UserDao;
import playground.exceptions.ChangeUserException;
import playground.exceptions.ConfirmException;
import playground.exceptions.LoginException;
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
	public ArrayList<UserEntity> getUsers() {
		ArrayList<UserEntity> lst = new ArrayList<UserEntity>();
			for(UserEntity u: userDB.findAll())
				lst.add(u);

		return lst;
	}

	@Override
	@Transactional
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
	public void cleanUserService() {
		userDB.deleteAll();

	}

	@Override
	@Transactional
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
	public UserEntity getUser(String email, String playground) {
		String idToSearchBy = UserEntity.setSuperkey(email, playground);
		UserEntity user = userDB.findById(idToSearchBy).orElse(null);
		return user;
	}

	@Override
	@Transactional(readOnly=true)
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
	@Transactional
	public void updateUser(UserEntity user, String email, String playground) {
		login(playground, email);
		if (getUser(email, playground).getRole().equals(Constants.MODERATOR_ROLE)) {
			if (user.getEmail().equals(email)) {
				updateUser(user);
			} else if (!user.getRole().equals(Constants.MODERATOR_ROLE)) {
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
			throw new ChangeUserException("Invalid role " + getUser(email, playground).getRole());
		}

	}

	@Override
	@Transactional
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
	@Transactional(readOnly=true)
	public void printUserDB() {
		ArrayList<UserEntity> lst = new ArrayList<UserEntity>();
		try {
			
			for(UserEntity u: userDB.findAll())
				lst.add(u);
		} catch (Exception e) {

		}
		System.err.println("DB-TEST:all users in database:\n");
		for (UserEntity u : lst) {
			System.out.println(u.toString());
		}
		System.out.println("\n");
	}

}
