package org.example.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class FacadeLoggingAspect {

    @Pointcut("execution(public * org.example.facade..*(..))")
    public void facadeMethods(){}


    @Before("facadeMethods")
    public void logMethodCall(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("Calling method: {} with arguments {}", methodName, Arrays.toString(args));
    }

    @AfterReturning(pointcut = "facadeMethods()", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("Method {} returned with value: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "facadeMethods()", throwing = "ex")
    public void logMethodException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();
        log.error("Method {} threw exception: {}", methodName, ex.getMessage(), ex);
    }

}
