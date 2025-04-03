package com.project.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AuditAspect {

    private static final AuditAspect aspect = new AuditAspect();

    public static AuditAspect aspectOf() {
        return aspect;
    }


    @Pointcut("execution(protected * *(..)) && !within(*Test)")
    public void controllerMethods() {

    }


    @After("controllerMethods() && args(request, response)")
    public void audit(JoinPoint joinPoint, HttpServletRequest request, HttpServletResponse response) {

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        System.out.println("User  " + request.getSession().getAttribute("user") + " executing " + className + "." + methodName);
    }


    @Around("controllerMethods() && args(req, resp)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, HttpServletRequest req, HttpServletResponse resp) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Method " + joinPoint.getSignature().getName() + " executed in " + duration + " ms");

        return result;
    }
}
