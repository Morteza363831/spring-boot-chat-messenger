package com.example.messenger.aop;

import com.example.messenger.utility.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionHandlingAspect {

    // tools
    private final LoggingUtil loggingUtil;


    @Pointcut("@annotation(com.example.messenger.aop.AfterThrowingException) || @within(com.example.messenger.aop.AfterThrowingException)")
    private void afterThrowing() {}

    @AfterThrowing(pointcut = "afterThrowing()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        loggingUtil.error(log, joinPoint.getTarget().getClass().getSimpleName(), ex.getMessage(), joinPoint.getSignature().getName());
        loggingUtil.debug(log, joinPoint.getTarget().getClass().getSimpleName(), ex.getMessage(), joinPoint.getSignature().getName(), ex);
    }
}
