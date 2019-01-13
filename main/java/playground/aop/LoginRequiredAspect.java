package playground.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.constants.User;
import playground.dal.UserDao;
import playground.logic.LoginException;
import playground.logic.UserEntity;
import playground.logic.UserService;

@Component
@Aspect
public class LoginRequiredAspect {

	private UserDao userDB;
	private UserService userService;

	@Autowired
	public LoginRequiredAspect(UserDao userDB, UserService userService) {
		this.userDB = userDB;
		this.userService = userService;
	}

	@MyLog
	@Around("@annotation(playground.aop.LoginRequired) && args(userPlayground, email,..)")
	public Object login(ProceedingJoinPoint joinPoint, String userPlayground, String email) throws Throwable {
		UserEntity u = userDB.findById(userService.createKey(email, userPlayground)).orElse(null);
		if (u == null)
			throw new LoginException(User.EMAIL_NOT_REGISTERED_ERROR + " " + email);
		else if (!u.isVerified())
			throw new LoginException(User.USER_NOT_VERIFIED_ERROR);
		Object o = joinPoint.proceed(joinPoint.getArgs());
		return o;
	}

}
