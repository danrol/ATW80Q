package playground.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

//todo manage the users with restriction from here
@Component
@Aspect
public class AuthenticationAspect {
	
	
	@Before("@annotation(playground.aop.MyAuthentication)")
	public void verify (JoinPoint joinPoint) {
		//todo 
		//check if the user is in the database 
		Object[] ob=joinPoint.getArgs();
		//if(ob[1].)
	}
	 

}
