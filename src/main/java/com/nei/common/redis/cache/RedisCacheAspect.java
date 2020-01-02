package com.nei.common.redis.cache;

import com.nei.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * AOP 控制 Redis 自动缓存和更新
 * 在需要使用redis缓存的类上添加 @RedisCache 注解
 * 切面方法会以select/get/query 开头的查询方法,获取方法名和参数拼接为key,存到redis.
 * 在执行add/insert/update 开头的方法时,则清空该类下的所有缓存.
 */
@Aspect
@Service
@Slf4j
public class RedisCacheAspect {

    private static final Integer TIME_OUT = 30; //redis 存活时长：分钟

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 定义切点为缓存注解
     **/
    @Pointcut("@within(com.nei.common.redis.cache.RedisCache)")
    public void queryCachePointcut() {}

    @Around("queryCachePointcut()")
    public Object Interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //类名
        String simpleName = joinPoint.getTarget().getClass().getSimpleName();
        //方法名
        String methodName = signature.getMethod().getName();
        //获取参数值
        Object[] args = joinPoint.getArgs();
        //获取参数名
//        String[] strings = signature.getParameterNames();
        //获取返回类型
//        Class returnType = signature.getReturnType();
        String key = StringUtils.joinWith(Constants.UNDERLINE,simpleName, methodName, Arrays.toString(args));
        Object data = getObject(beginTime, joinPoint, key);
        if (data != null) {
            return data;
        }

        // 执行原方法
        return joinPoint.proceed();
    }

    /**
     * 使用key获取数据  不存在则查询添加
     * @param beginTime : 切面开始时间
     * @param joinPoint : 切面对象
     * @param key       : 获取redis数据的key值
     * @return java.lang.Object
     **/
    private Object getObject(long beginTime, ProceedingJoinPoint joinPoint, String key) throws Throwable {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        Boolean hasKey = redisTemplate.hasKey(key);
        Object obj;
        if (hasKey != null && hasKey) {
            // 缓存中获取到数据，直接返回。
            obj = operations.get(key);
            log.info("从缓存中获取到 key [" + key + " ] 的数据 > " + obj);
            log.info("AOP 缓存切面处理 > end 耗时：" + (System.currentTimeMillis() - beginTime));
        } else {
            // 缓存中没有数据，调用原始方法查询数据库
            obj = joinPoint.proceed();
            operations.set(key, obj, TIME_OUT, TimeUnit.MINUTES); // 设置超时时间30分钟
            log.info("向 Redis 添加 key [" + key + " ] , 存活时长 " + TIME_OUT + " min 的数据 > " + obj);
            log.info("AOP 缓存切面处理 > end 耗时：" + (System.currentTimeMillis() - beginTime));
        }
        return obj;
    }

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();//序列化为String
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);//序列化为Json
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        this.redisTemplate = redisTemplate;
    }

}
