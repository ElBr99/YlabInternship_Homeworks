package com.project.aop;

import com.project.utils.SecurityContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

//@Aspect
//@Component
public class AuditAspect {

    @Pointcut("execution(* com.project.controller..*(..))")
    public void controllerMethods() {

    }

    @After("controllerMethods()")
    public void audit(JoinPoint joinPoint) {

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        System.out.println("User  " + SecurityContext.getCurrentUserEmail() + " executing " + className + "." + methodName);
    }


    @Around("controllerMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Method " + joinPoint.getSignature().getName() + " executed in " + duration + " ms");

        return result;
    }
}
