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

	private IdGeneratorUserDao IdGeneratorUser;

	@Autowired
	public jpaUserService(UserDao userDB,IdGeneratorUserDao IdGeneratorUser) {
		this.userDB = userDB;
		this.IdGeneratorUser = IdGeneratorUser;
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
		if (userDB.existsById(user.getSuperkey())) {
			throw new RegisterNewUserException("User exists with name: " + user.getSuperkey());
		} else 
		{
			IdGeneratorUser tmp = IdGeneratorUser.save(new IdGeneratorUser());
			Long id = tmp.getId();
			IdGeneratorUser.delete(tmp);
			user.setId(id+"");
			userDB.save(user);
			return userDB.findById(user.getSuperkey()).orElse(null);
		}
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
		UserEntity u =  getUser(email, playground);
		if (u.equals(user)) {
			updateUser(user);
		}
		else throw new PermissionUserException("User " + u + " can't access another user");

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
