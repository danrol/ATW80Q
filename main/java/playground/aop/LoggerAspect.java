package playground.aop;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.sun.org.apache.bcel.internal.generic.LNEG;

@Component
@Aspect
public class LoggerAspect {
	
//	@Before("@annotation(playground.aop.MyLog)")
	public void log (JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		System.err.println("*****************" + className + "." + methodName + "()");
	}
	
	@Around("@annotation(playground.aop.MyLog)")
	public Object log (ProceedingJoinPoint joinPoint) throws Throwable {
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		String methodSignature = className + "." + methodName + "()";
		System.err.println("*");
		System.err.println("**");
		System.err.println("***");
     	System.err.println(methodSignature + " - start");
		
		
		try {
			Object rv = joinPoint.proceed();
			System.err.println(methodSignature + " - ended successfully");
			System.err.println("***");
			System.err.println("**");
			System.err.println("*");
			return rv;
		} catch (Throwable e) {
			System.err.println(methodSignature + " - end with error" + e.getClass().getName());
			System.err.println("***");
			System.err.println("**");
			System.err.println("*");
			throw e;
		}
	}
	
//	@Around("@annotation(playground.aop.MyLog) && args(name,..)")
	public Object log (ProceedingJoinPoint joinPoint, String name) throws Throwable {
		System.err.println("name: " + name);
		if (name == null || name.length() < 5) {
			throw new RuntimeException("Invalid name: " + name + " - it is too short");
		}
		return joinPoint.proceed();
	}
}
