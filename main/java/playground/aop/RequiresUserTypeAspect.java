package playground.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;

import playground.dal.UserDao;
import playground.exceptions.LoginException;
import playground.exceptions.PermissionUserException;
import playground.logic.UserEntity;

public class RequiresUserTypeAspect {

	private UserDao userDB;

	@Autowired
	public RequiresUserTypeAspect(UserDao userDB) {
		this.userDB = userDB;
	}
	
	@Around("@annotation(playground.aop.RequiresUserType) && args(userPlayground,email,..)")
	public Object checkPermission(ProceedingJoinPoint joinPoint, String userPlayground, String email, 
			String requiredRole) throws Throwable {
		//TODO change needed
		UserEntity u = userDB.findById(UserEntity.createKey(email, userPlayground)).orElse(null);
		System.err.println(u);
		if(u.getRole().equals(requiredRole)){
			Object o = joinPoint.proceed(joinPoint.getArgs());
			return o;
		}else {
			throw new PermissionUserException("User is not verified.");
		}
		
	}
}
