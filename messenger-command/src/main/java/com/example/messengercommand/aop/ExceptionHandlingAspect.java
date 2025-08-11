package com.example.messengercommand.aop;

import com.example.messengercommand.utility.LoggingUtil;
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

    private final LoggingUtil loggingUtil;

    @Pointcut("@annotation(com.example.messengercommand.aop.AfterThrowingException) || @within(com.example.messengercommand.aop.AfterThrowingException)")
    public void afterThrowing() {}

    @AfterThrowing(pointcut = "afterThrowing()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        final Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        loggingUtil.error(log, joinPoint.getTarget().getClass().getSimpleName(), ex.getMessage(), joinPoint.getSignature().getName());
        loggingUtil.debug(log, joinPoint.getTarget().getClass().getSimpleName(), ex.getMessage(), joinPoint.getSignature().getName(), ex);
    }
}
