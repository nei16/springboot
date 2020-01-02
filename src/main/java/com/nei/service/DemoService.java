package com.nei.service;

import com.battcn.boot.extend.configuration.redis.RedisParam;
import com.nei.common.redis.lock.RedisLock;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by LiBo on 2019-10-12
 */
@Slf4j
@Service
public class DemoService {

    private char[] chars = {'0', '1', '2', '3', '4', '5','6','7','8','9','a','b','c','d','e','f'};

    /**
     * key: p1_
     */
    @RedisLock(prefix = "p1_")
    public void print1(int i){
        log.info("{}", i);
    }

    /**
     * RedisParam 将参数作为key的一部分
     * key: p2_${i}
     */
    @RedisLock(prefix = "p2_")
    public void print2(@RedisParam int i){
        log.info("{}", i);
    }

    /**
     * key: p3_
     */
    @SneakyThrows
    @RedisLock(prefix = "p3_")
    public void print3(int i){
        log.info("{}", i);
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            System.out.print(c);
            Thread.sleep(100);
        }
        System.out.println();
    }

}
