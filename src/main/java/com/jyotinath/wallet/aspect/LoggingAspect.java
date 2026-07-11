package com.jyotinath.wallet.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger =
            LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.jyotinath.wallet.service.*.*(..))")
    public void beforeMethodExecution(JoinPoint joinPoint) {

        logger.info(
                "Method Started : {} with arguments {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(
            pointcut = "execution(* com.jyotinath.wallet.service.*.*(..))",
            returning = "result")
    public void afterMethodExecution(
            JoinPoint joinPoint,
            Object result) {

        logger.info(
                "Method Completed : {}",
                joinPoint.getSignature().getName());

        logger.info(
                "Return Value : {}",
                result);
    }

    @AfterThrowing(
            pointcut = "execution(* com.jyotinath.wallet.service.*.*(..))",
            throwing = "exception")
    public void logException(
            JoinPoint joinPoint,
            Exception exception) {

        logger.error(
                "Exception in Method : {}",
                joinPoint.getSignature().getName());

        logger.error(
                "Exception Message : {}",
                exception.getMessage());
    }

    @Around("execution(* com.jyotinath.wallet.service.*.*(..))")
    public Object measureExecutionTime(
            ProceedingJoinPoint joinPoint)
            throws Throwable {

        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();

        logger.info(
                "{} executed in {} ms",
                joinPoint.getSignature().getName(),
                (endTime - startTime));

        return result;
    }
}