package playground.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.dal.UserDao;
import playground.logic.UserEntity;

@Component
@Aspect
public class LoginRequiredAspect {
	private UserDao userDB;

	@Autowired
	public LoginRequiredAspect(UserDao userDB) {
		this.userDB = userDB;
	}

	@Around("@annotation(playground.aop.LoginRequired) && args(userPlayground,email,..)")
	public void Login(ProceedingJoinPoint joinPoint, String userPlayground, String email) throws Throwable {
		if (userPlayground == null) {
			throw new RuntimeException("Invalid name: " + " - it is too short");
		}

		// UserEntity u = userDB.findById(UserEntity.createKey(email,
		// userPlayground)).orElse(null);

		System.err.println("Login here UserPlayground: " + userPlayground + "and email: " + email);
		// System.err.println("User is " + u);
		joinPoint.proceed();
	}

}
