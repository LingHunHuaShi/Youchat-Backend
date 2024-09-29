package com.zzh.youchatbackend.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthFilter {
    /**
     * 是否需要检查登录
     * @return 是/否
     */
    boolean checkLogin() default true;

    /**
     * 是否需要检查管理员权限
     * @return 是/否
     */
    boolean checkAdmin() default false;
}
