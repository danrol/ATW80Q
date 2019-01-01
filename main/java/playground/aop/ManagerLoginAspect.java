package playground.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.Constants;
import playground.dal.UserDao;
import playground.exceptions.LoginException;
import playground.exceptions.PermissionUserException;
import playground.logic.UserEntity;


@Component
@Aspect
public class ManagerLoginAspect {

	private UserDao userDB;

	@Autowired
	public ManagerLoginAspect(UserDao userDB) {
		this.userDB = userDB;
	}
	
	@Around("@annotation(playground.aop.ManagerLogin) && args(userPlayground,email,..)")
	public Object checkPermission(ProceedingJoinPoint joinPoint, String userPlayground, String email) throws Throwable {
		UserEntity u = userDB.findById(UserEntity.createKey(email, userPlayground)).orElse(null);
		if (u == null) 
			throw new LoginException("Email is not registered.");
			else if(!u.isVerified()) 
				throw new LoginException("User is not verified.");
			else if(u.getRole() != Constants.MANAGER_ROLE)
				throw new PermissionUserException("User" + u.getRole() + " has no access rights.");
		System.err.println("Manager Login: User " + u + "logged in.");
		Object o = joinPoint.proceed(joinPoint.getArgs());
		return o;
		
	}
}
