package playground.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.constants.Constants;
import playground.dal.UserDao;
import playground.logic.LoginException;
import playground.logic.PermissionUserException;
import playground.logic.UserEntity;
import playground.logic.UserService;

@Component
@Aspect
public class PlayerLoginAspect {
	private UserDao userDB;
	private UserService userService;
	@Autowired
	public PlayerLoginAspect(UserDao userDB, UserService userService) {
		this.userDB = userDB;
		this.userService=userService;
	}
	@MyLog
	@Around("@annotation(playground.aop.PlayerLogin) && args(userPlayground,email,..)")
	public Object Login(ProceedingJoinPoint joinPoint, String userPlayground, String email) throws Throwable {
		UserEntity u = userDB.findById(userService.createKey(email, userPlayground)).orElse(null);
		if (u == null) 
			throw new LoginException("Email is not registered.");
		else if(!u.isVerified()) 
				throw new LoginException("User is not verified.");
		else if(u.getRole() != Constants.PLAYER_ROLE)
				throw new PermissionUserException("User" + u.getRole() + "has no access rights.");
		System.err.println("Player Login Required: User " + u + "logged in.");
		Object o = joinPoint.proceed(joinPoint.getArgs());
		return o;
	}
}
