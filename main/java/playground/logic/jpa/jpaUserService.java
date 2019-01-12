package playground.logic.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import playground.aop.LoginRequired;
import playground.aop.MyLog;
import playground.constants.Playground;
import playground.constants.User;
import playground.dal.UserDao;
import playground.logic.ActivityDataException;
import playground.logic.ActivityEntity;
import playground.logic.ConfirmException;
import playground.logic.ElementEntity;
import playground.logic.NewUserForm;
import playground.logic.PermissionUserException;
import playground.logic.RegisterNewUserException;
import playground.logic.UserDataException;
import playground.logic.UserEntity;
import playground.logic.UserService;

@Service
public class jpaUserService implements UserService {

	// this is the database we need are saving in
	private UserDao userDB;
	private IdGeneratorUserDao IdGeneratorUser;
	private EmailService emailService;
	
	@Autowired
	public jpaUserService(UserDao userDB, IdGeneratorUserDao IdGeneratorUser, EmailService emailService) {
		this.userDB = userDB;
		this.IdGeneratorUser = IdGeneratorUser;
		this.emailService = emailService;
	}

	@Override
	@Transactional
	@MyLog
	public ArrayList<UserEntity> getUsers() {
		ArrayList<UserEntity> lst = new ArrayList<UserEntity>();
		for (UserEntity u : userDB.findAll())
			lst.add(u);

		return lst;
	}

	@Override
	@Transactional
	@MyLog
	public UserEntity[] getUsers(Pageable pageable) {
		List<UserEntity> allUsers = userDB.findAll(pageable).getContent();
		return turnListIntoArray(allUsers);
	}

	public UserEntity[] turnListIntoArray(List<UserEntity> lst) {
		return lst.toArray(new UserEntity[lst.size()]);
	}

	@Override
	@Transactional
	@MyLog
	public UserEntity addUser(UserEntity user) {
		if (userDB.existsById(user.getSuperkey()))
			throw new RegisterNewUserException(User.DUPLICATE_USER_KEY_ERROR + user.getSuperkey());
		else {
			IdGeneratorUser tmp = IdGeneratorUser.save(new IdGeneratorUser());
			Long id = tmp.getId();
			IdGeneratorUser.delete(tmp);
			user.setId(id + "");
			userDB.save(user);
			if(!user.isVerified() && Playground.MESSAGE_SENDER_ENABLED == true)
			{
				
				this.sendVerificationCodeToMail(user);
			}
				
			return user;
		}
	}

	@Override
	public UserEntity createUserEntity(String json) {
		UserEntity user = null;
		try {
			user = new UserEntity(json);
		} catch (Exception e) {
			throw new UserDataException(e.getMessage());
		}
		return user;
	}

	@Override
	@Transactional
	@MyLog
	public UserEntity verifyUser(String email, String playground, String code) {
		UserEntity user = getUser(playground, email);
		if (user != null) {
			if (user.isVerified())
				return user; // User already confirmed
			else if (user.getPlayground().equals(playground)) {
				String VerificationCode = user.getVerificationCode();
				if (VerificationCode.equals(code))
					user.verifyUser();
				else
					throw new ConfirmException(User.VERIFICATION_CODE_MISMATCH_ERROR);
			} else
				throw new ConfirmException(
						User.USER_NOT_IN_PLAYGROUND_ERROR + user.getEmail() + "  (" + playground + ")");
		} else {
			throw new ConfirmException(User.EMAIL_NOT_REGISTERED_ERROR);
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

			UserEntity oldUser = this.getUser(user.getPlayground(), user.getEmail());
			if (oldUser.isVerified())
				user.verifyUser();
			String id = oldUser.getId();
			userDB.deleteById(user.getSuperkey());
			user.setId(id);
			userDB.save(user);
		}

	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public UserEntity getUser(String playground, String email) {
		return getUser(createKey(email, playground));
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	@LoginRequired
	public UserEntity login(String playground, String email) {
		return this.getUser(playground, email);
	}

	@Override
	@Transactional
	@MyLog
	@LoginRequired
	public void updateUser(String playground, String email, UserEntity user) {

		UserEntity u = getUser(playground, email);
		if (u.getSuperkey().equals(user.getSuperkey()))
			updateUser(user);
		else
			throw new PermissionUserException(User.UPDATE_ANOTHER_USER_ERROR);

	}

	@Override
	@Transactional
	@MyLog
	public void addUser(NewUserForm user) {
		if (this.getUser(Playground.PLAYGROUND_NAME, user.getEmail()) != null)
			throw new RegisterNewUserException(User.USER_ALREADY_REGISTERED_ERROR);
		else {
			UserEntity userEnt = new UserEntity(user.getUsername(), user.getEmail(), user.getAvatar(), user.getRole(),
					Playground.PLAYGROUND_NAME);
			addUser(userEnt);
		}
	}

	@Override
	@Transient
	@MyLog
	public boolean isUserInDatabase(UserEntity user) {
		return this.userDB.existsById(user.getSuperkey());
	}

	@Override
	public void addPointsToUser(String user_id, long points) {
		UserEntity user = this.getUser(user_id);
		long curr_points = this.getUser(user_id).getPoints() + points;
		user.setPoints(curr_points);
		updateUser(user);
	}

	@Override
	public UserEntity getUser(String superkey) {
		UserEntity t = userDB.findById(superkey).orElse(null);
		if(t==null)
			throw new UserDataException(User.EMAIL_NOT_REGISTERED_ERROR);
		return t;

	}

	@Override
	public String createKey(String email, String playground) {
		return email.concat(" " + playground);
	}

	@Override
	public UserEntity[] getHighScoresFromHighestToLowest(Pageable pageable) {
		return lstToArray(userDB.findAllByOrderByPointsDesc(pageable));
	}
	
	@Override
	@Transactional(readOnly = true)
	@MyLog
	public UserEntity[] lstToArray(ArrayList<UserEntity> lst) {
		return lst.toArray(new UserEntity[lst.size()]);
	}
	@MyLog
	public void sendVerificationCodeToMail(UserEntity user)
	{
		Mail mail = new Mail();
		mail.setTo(user.getEmail());
		mail.setSubject(Playground.VERIFICATION_MAIL_SUBJECT);
		mail.setContent(Playground.getVerificationMailContent(user.getUsername(), user.getVerificationCode(), user.getEmail(),user.getPlayground()));
		
		try {
			emailService.sendMail(mail);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
		
	}


}
