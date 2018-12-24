package playground.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;

import playground.Constants;
import playground.dal.UserDao;
import playground.exceptions.LoginException;
import playground.exceptions.PermissionUserException;
import playground.logic.UserEntity;

public class ModeratorLoginAspect {

	private UserDao userDB;

	@Autowired
	public ModeratorLoginAspect(UserDao userDB) {
		this.userDB = userDB;
	}
	
	@Around("@annotation(playground.aop.ModeratorLogin) && args(userPlayground,email,..)")
	public Object checkPermission(ProceedingJoinPoint joinPoint, String userPlayground, String email, 
			String requiredRole) throws Throwable {
		UserEntity u = userDB.findById(UserEntity.createKey(email, userPlayground)).orElse(null);
		if (u == null) 
			throw new LoginException("Email is not registered.");
			else if(!u.isVerified()) 
				throw new LoginException("User is not verified.");
			else if(u.getRole() != Constants.MODERATOR_ROLE)
				throw new LoginException("User" + u.getRole() + "has no access rights.");
				
		Object o = joinPoint.proceed(joinPoint.getArgs());
		return o;
		
	}
}
