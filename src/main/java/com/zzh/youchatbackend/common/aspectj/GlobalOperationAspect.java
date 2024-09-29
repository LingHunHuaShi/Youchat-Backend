package com.zzh.youchatbackend.common.aspectj;

import com.zzh.youchatbackend.common.entity.query.UserQuery;
import com.zzh.youchatbackend.common.token.JwtTokenUtils;
import com.zzh.youchatbackend.module.common.service.UserService;
import com.zzh.youchatbackend.common.annotation.AuthFilter;
import com.zzh.youchatbackend.common.entity.po.User;
import com.zzh.youchatbackend.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.jose4j.jwt.JwtClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class GlobalOperationAspect {
    JwtTokenUtils jwtTokenUtils;
    Environment env;
    UserService userService;

    @Autowired
    GlobalOperationAspect(JwtTokenUtils jwtTokenUtils, Environment env, UserService userService) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.env = env;
        this.userService = userService;
    }

    @Before("@annotation(com.zzh.youchatbackend.common.annotation.AuthFilter)")
    public void authFilter(JoinPoint joinPoint) {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        AuthFilter authFilter = method.getAnnotation(AuthFilter.class);
        if (authFilter == null) {
            return;
        }
        // 拦截请求，对于需要检查登录的请求检查登录
        if (authFilter.checkLogin()) {
            checkLogin(authFilter.checkAdmin());
        }
    }

    private void checkLogin(Boolean checkAdmin) {
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (attributes == null) {
            log.info("Null attribute object.");
            throw new BusinessException("Internal server error");
        }
        // 从请求头中获取 token string
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        // 检查 token 有效性
        JwtClaims claims = jwtTokenUtils.verifyToken(token);
        if (claims == null) {
            throw new BusinessException("Invalid token.");
        }
        log.info("Claims of token: {}", claims.toJson());


        // 如果需要管理员权限，则检查是否为管理员
        if (checkAdmin) {
            String sub;
            String[] admins;
            try {
                sub = claims.getSubject();
                admins = env.getProperty("admin.email").split(",");
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new BusinessException("Internal server error.");
            }

            UserQuery userQuery = UserQuery.builder()
                    .uid(sub)
                    .build();
            User subjectUser = userService.getUser(userQuery);

            if (!Arrays.asList(admins).contains(subjectUser.getEmail())) {
                throw new BusinessException("Unauthorized: User is not an admin");
            }
        }
    }
}
