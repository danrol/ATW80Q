package playground.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.dal.UserDao;
import playground.exceptions.LoginException;
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
	public Object Login(ProceedingJoinPoint joinPoint, String userPlayground, String email) throws Throwable {

		UserEntity u = userDB.findById(UserEntity.createKey(email, userPlayground)).orElse(null);
		System.err.println(u);
		if (u != null) {
			if (u.isVerified()) {
				Object o = joinPoint.proceed(joinPoint.getArgs());
				return o;
				
			} else {
				throw new LoginException("User is not verified.");
			}

		} else {
			throw new LoginException("Email is not registered.");
		}
	}

}
