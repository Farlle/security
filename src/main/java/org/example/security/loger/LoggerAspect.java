package org.example.security.loger;

import java.util.logging.Logger;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {
    private static final Logger logger = Logger.getLogger(LoggerAspect.class.getName());

    @After("execution(* org.example.controller..*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("Method executed  " + joinPoint.getSignature().getName());
    }

}