package es.mindata.w2m.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Aspect
@Component
public class TimedExecutionAspect {

	@Around("@annotation(es.mindata.w2m.annotations.TimedExecution)")
	public Object timeMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.nanoTime();
		Object result = joinPoint.proceed();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
		log.info("Método: {} - Tiempo total de ejecución: {} milisegundos", joinPoint.getSignature().toShortString(), duration);
		return result;
	}

}
