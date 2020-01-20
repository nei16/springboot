package com.nei.common.aspect;

import com.nei.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * 打印请求日志
 */
@Aspect
@Slf4j
@Component
public class LogRequestAspect {

    @Pointcut("execution(* com.nei.controller..*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before() {
        HttpServletRequest request = getHttpServletRequest();
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = getHttpServletRequest();
        long endTime = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        Object[] args = pjp.getArgs();
        Object result = pjp.proceed();
        String response = JsonUtil.json(result);
        long startTime = (long) request.getAttribute("startTime");
        String params = "";
        if (args.length > 0) {
            if ("GET".equals(method)) {
                params = request.getQueryString();
            } else {
                Object object = args[0];
                params = JsonUtil.json(object);
            }
        }
        log.info("{}, {}, params:{}, response:{}, elapsed:{}ms.", method, uri, params, response, (endTime - startTime));
        return result;
    }

    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
    }

}
