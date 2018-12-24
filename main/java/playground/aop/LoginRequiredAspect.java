package playground.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;

import playground.Constants;
import playground.dal.UserDao;
import playground.exceptions.LoginException;
import playground.exceptions.PermissionUserException;
import playground.logic.UserEntity;

public class LoginRequiredAspect {

	private UserDao userDB;

	@Autowired
	public LoginRequiredAspect(UserDao userDB) {
		this.userDB = userDB;
	}
	
	@Around("@annotation(playground.aop.LoginRequired) && args(userPlayground,email,..)")
	public Object checkPermission(ProceedingJoinPoint joinPoint, String userPlayground, String email, 
			String requiredRole) throws Throwable {
		UserEntity u = userDB.findById(UserEntity.createKey(email, userPlayground)).orElse(null);
		if (u == null) 
			throw new LoginException("Email is not registered.");
			else if(!u.isVerified()) 
				throw new LoginException("User is not verified.");
				
		Object o = joinPoint.proceed(joinPoint.getArgs());
		return o;
		
	}
}
