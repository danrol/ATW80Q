package playground.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import playground.constants.User;
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
			throw new LoginException(User.EMAIL_NOT_REGISTERED_ERROR);
		else if(!u.isVerified()) 
				throw new LoginException(User.USER_NOT_VERIFIED_ERROR);
		else if(u.getRole() != User.PLAYER_ROLE)
				throw new PermissionUserException(u.getRole() + User.LOGIN_ASPECT_ACCESS_RIGHTS_ERROR + joinPoint.getSignature().getDeclaringTypeName());
		Object o = joinPoint.proceed(joinPoint.getArgs());
		return o;
	}
}
