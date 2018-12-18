package playground.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoginRequiredAspect {
	

	@Before("@annotation(playground.aop.LoginRequired) && args(userPlayground,email,..)")
	public void Login(ProceedingJoinPoint joinPoint, String userPlayground, String email) throws Throwable {
		//TODO
		
		System.err.println("Login here UserPlayground: " + userPlayground + "and email: " + email);
		if (userPlayground == null || email.length() < 5) {
			throw new RuntimeException("Invalid name: " + email + " - it is too short");
		}
	}
	 

}
