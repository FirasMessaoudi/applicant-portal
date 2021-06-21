/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.audit;

import com.elm.shj.applicant.portal.services.audit.AuditLogService;
import com.elm.shj.applicant.portal.services.dto.AuditLogDto;
import com.elm.shj.applicant.portal.web.security.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;

/**
 * Aspect for automatically logging request in auditLog in the database.
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuditLogAspect {

    private final AuditLogService auditLogService;

    /**
     * Pointcut for rest controllers
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
    }

    /**
     * Pointcut for public methods
     */
    @Pointcut("execution(public * *(..))")
    protected void publicOperation() {
    }

    /**
     * Creates a proxy around public methods in rest controllers to track user actions and save them in the database
     *
     * @param joinPoint the point details
     * @return the result of the operation
     * @throws Throwable if any issue happened during proceeding
     */
    @Around("restController() && publicOperation()")
    public Object logPublicControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object ret = joinPoint.proceed(joinPoint.getArgs());

        long processingTime = System.currentTimeMillis() - startTime;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuditLogDto auditLog = AuditLogDto.builder()
                .action(joinPoint.getSignature().getName())
                .handler(joinPoint.getSignature().getDeclaringType().getSimpleName())
                .channel(request.getHeader("User-Agent"))
                .host(request.getRemoteHost())
                .origin(request.getRequestURL().toString())
                .startTime(new Date(startTime))
                .processingTime(processingTime)
                .httpStatus(response != null ? response.getStatus() : -1)
                .params(Arrays.toString(joinPoint.getArgs())
                        .replaceAll("password=.*\\}(.*)", "password=<protected>\\}$1")
                        .replaceAll("password=.*,(.*)", "password=<protected>,$1")
                        .replaceAll("token=.*,(.*)", "token=<protected>,$1")
                        .replaceAll("token\":.*\\}(.*)", "token\":<protected>\\}$1")
                        .replaceAll("credentials\":.*,(.*)", "credentials\":<protected>,$1")
                        .replaceAll("credentials\":.*\\}(.*)", "credentials\":<protected>\\}$1"))
                .userIdNumber(!(authentication instanceof JwtToken) ? -1 : Integer.parseInt(authentication.getName()))
                .build();

        log.debug(
                "\n================================================================================================\n"
                        + auditLog +
                        "\n================================================================================================\n"
        );

        auditLogService.save(auditLog);
        return ret;
    }

    /**
     * Creates a proxy around public methods in rest controllers to log thrown exceptions and save them in the database
     *
     * @param joinPoint the point details
     * @param exception the thrown exception
     */
    @AfterThrowing(pointcut = "restController() && publicOperation()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuditLogDto auditLog = AuditLogDto.builder()
                .action(joinPoint.getSignature().getName())
                .handler(joinPoint.getSignature().getDeclaringType().getSimpleName())
                .channel(request.getHeader("User-Agent"))
                .host(request.getRemoteHost())
                .origin(request.getRequestURL().toString())
                .startTime(new Date())
                .processingTime(-1)
                .httpStatus(response != null ? response.getStatus() : -1)
                .params(Arrays.toString(joinPoint.getArgs()).replaceAll("password=.*\\}(.*)", "password=<protected>\\}$1").replaceAll("password=.*,(.*)", "password=<protected>,$1"))
                .userIdNumber(!(authentication instanceof JwtToken) ? -1 : Integer.parseInt(authentication.getName()))
                .errorDetails(exception.getClass().getSimpleName() + ":" + exception.getMessage() + "::" + ((ArrayUtils.getLength(exception.getStackTrace()) > 0) ? exception.getStackTrace()[0] : ""))
                .build();

        log.debug(
                "\n================================================================================================\n"
                        + auditLog +
                        "\n================================================================================================\n"
        );

        auditLogService.save(auditLog);
    }

}
