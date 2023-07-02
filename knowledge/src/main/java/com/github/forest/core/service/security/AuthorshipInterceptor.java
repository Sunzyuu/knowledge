package com.github.forest.core.service.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.forest.enumerate.Module;
/**
 * @author sunzy
 * @date 2023/7/2 15:10
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorshipInterceptor {
    Module moduleName();
}

