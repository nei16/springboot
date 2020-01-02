package com.nei.common.redis.lock;

import com.battcn.boot.extend.configuration.redis.RedisKeyGenerator;
import com.battcn.boot.extend.configuration.redis.lock.RedisLockHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by LiBo on 2019-10-12
 */
@Slf4j
@Aspect
@Component
public class RedisLockAspect {

    private static final String DELIMITER = "|";

    @Resource
    private RedisLockHelper redisLockHelper;
    @Resource
    private RedisKeyGenerator redisKeyGenerator;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分布式锁确保只会有一个线程执行成功
     */
    @Around("execution(public * *(..)) && @annotation(com.nei.common.redis.lock.RedisLock)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RedisLock lock = method.getAnnotation(RedisLock.class);
        if (StringUtils.isEmpty(lock.prefix())) {
            throw new RuntimeException("lock key prefix don't null...");
        }
        String lockKey = redisKeyGenerator.generate(lock.prefix(), lock.delimiter(), pjp);
        String uuid = UUID.randomUUID().toString();
        try {
            boolean flag = redisLockHelper.lock(lockKey, uuid, lock.expire(), lock.timeUnit());
            if (!flag) {
                log.warn("lock failed");
                return null;
            }
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                log.error("[server exception]", throwable);
                throw new RuntimeException("server exception");
            }
        } finally {
            redisLockHelper.unlock(lockKey, uuid);
        }
    }

    /**
     * 保证多个线程依次执行，不会同时执行
     */
//    @Around("execution(public * *(..)) && @annotation(com.nei.common.redis.lock.RedisLock)")
    public Object interceptor1(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RedisLock lock = method.getAnnotation(RedisLock.class);
        if (StringUtils.isEmpty(lock.prefix())) {
            throw new RuntimeException("lock key prefix don't null...");
        }
        String lockKey = redisKeyGenerator.generate(lock.prefix(), lock.delimiter(), pjp);
        String uuid = UUID.randomUUID().toString();
        try {
//            tryLock1(lockKey, uuid, lock.expire(), lock.timeUnit());
            tryLock2(lockKey, uuid, lock.expire(), lock.timeUnit());

            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                log.error("[server exception]", throwable);
                throw new RuntimeException("server exception");
            }
        } finally {
//            unlock1(lockKey, uuid);
            unlock2(lockKey, uuid);
        }
    }

    /**
     * 自旋锁
     */
    @SneakyThrows
    public void tryLock1(String key, String value, long timeout, TimeUnit unit){
        while (true) {
            boolean flag = redisLockHelper.tryLock(key, value, timeout, unit);
            if (flag) {
                return;
            }
            Thread.sleep(100);
        }
    }

    /**
     * 自旋锁
     */
    @SneakyThrows
    public void tryLock2(String key, String value, long timeout, TimeUnit unit){
        while (true) {
            boolean flag = lock2(key, value, timeout, unit);
            if (flag) {
                return;
            }
            Thread.sleep(100);
        }
    }

    public boolean lock2(String key, String uuid, long timeout, TimeUnit unit){
        long milliseconds = Expiration.from(timeout, unit).getExpirationTimeInMilliseconds();
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(key, (System.currentTimeMillis() + milliseconds) + DELIMITER + uuid, timeout, unit);
        return success != null && success;
    }

    /**
     * 对应 tryLock1() 的解锁
     */
    public void unlock1(String key, String value) {
        stringRedisTemplate.execute((RedisCallback<Boolean>) connection -> {
            byte[] bytes = connection.get(key.getBytes());
            if (Arrays.equals(bytes, value.getBytes())) {
                connection.del(key.getBytes());
            }
            return true;
        });
    }

    /**
     * 对应 tryLock2() 的解锁
     */
    public void unlock2(String key, String value) {
        String val = stringRedisTemplate.opsForValue().get(key);
        if (val == null) {
            return;
        }
        String[] values = val.split(Pattern.quote(DELIMITER));
        if (values.length <= 0) {
            return;
        }
        if (value.equals(values[1])) {
            stringRedisTemplate.delete(key);
        }
    }

}
