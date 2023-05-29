package com.github.forest.config;

import com.github.forest.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * 实现web层的日志切面
 * @Author sunzy
 * @Date 2023/5/29 16:28
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    /**
     * 定义一个切点
     *
     * <p>~ 第一个 * 代表任意修饰符及任意返回值. ~ 第二个 * 任意包名 ~ 第三个 * 代表任意方法.
     *<p>~ 第四个 * 定义在web包或者子包 ~ 第五个 * 任意方法 ~ .. 匹配任意数量的参数. execution(*
     **/
    @Pointcut("execution(* com.github.forest.*.api.*.*.*(..))")
    public void webLog(){
    }


    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        log.info("WebLogAspect.doBefore()");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录请求信息
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + Utils.getIpAddress(request));
        log.info("CLASS_METHOD : "
                        + joinPoint.getSignature().getDeclaringTypeName()
                        + "."
                        + joinPoint.getSignature().getName()
                );
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs())
                .replaceAll("(?<=password).*?(?=(nickname|$))", "=****, ")
                .replaceAll("(?<=password).*?(?=(\\)|$))", "=****)]")
                .replaceAll("(?<=password).*?(?=(code|$))", "=****, "));

        Enumeration<String> enu = request.getParameterNames();
        // 打印所有参数 过滤密码信息
        while (enu.hasMoreElements()) {
            String paramName = enu.nextElement();
            if("password".equals(paramName)) {
                continue;
            }
            log.info(paramName + ": " + request.getParameter(paramName));
        }
    }

    @AfterReturning("webLog()")
    public void doAfterReturning(JoinPoint joinPoint) {
        log.info("WebLogAspect.doAfterReturning()");
        log.info("耗时（毫秒） : " + (System.currentTimeMillis() - startTime.get()));
    }

}
