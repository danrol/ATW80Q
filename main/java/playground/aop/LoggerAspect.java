package playground.aop;

import java.util.Arrays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;



@Component
@Aspect
public class LoggerAspect {
	private Log log = LogFactory.getLog(LoggerAspect.class);

	
	
	@Around("@annotation(playground.aop.MyLog)")
	public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		String methodSignature = className + "." + methodName + "()";
		System.err.println("playground logger --------->>>");
		log.debug(methodSignature + " - start with args " + Arrays.toString(joinPoint.getArgs()));

		try {
			Object rv = joinPoint.proceed();
			log.debug(methodSignature + " - ended successfully");
			System.err.println("playground logger OUT--------->>>");
			
			return rv;
		} catch (Throwable e) {
			log.error(methodSignature + " - end with error" + e.getClass().getName());

			throw e;
		}
	}


}
