package playground.aop;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import playground.dal.UserDao;
import playground.exceptions.LoginException;
import playground.logic.UserEntity;
import playground.logic.UserService;


@Component
@Aspect
public class LoginRequiredAspect {
	


private UserDao userDB;
private UserService userService;

@Autowired
public LoginRequiredAspect(UserDao userDB , UserService userService) {
	this.userDB = userDB;
	this.userService = userService;
}
	
	@Around("@annotation(playground.aop.LoginRequired)")
	public Object log(ProceedingJoinPoint joinPoint) throws Throwable {//String userPlayground, String email, String requiredRole
		System.err.println("IN HERE HELLO LOOK AT ME!");
//		UserEntity u = userDB.findById(UserEntity.createKey(email, userPlayground)).orElse(null);
//		if (u == null) 
//			throw new LoginException("Email is not registered.");
//			else if(!u.isVerified()) 
//				throw new LoginException("User is not verified.");
//				
		Object o = joinPoint.proceed(joinPoint.getArgs());
		return o;
	}



	}

