package com.nei;

import com.google.common.collect.Lists;
import com.nei.service.DemoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisLockTest {

    @Autowired
    private DemoService demoService;

    @SneakyThrows
    @Test
    public void test1() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executorService.execute(() -> demoService.print1(finalI));
        }

        checkActiveCount((ThreadPoolExecutor) executorService);
    }

    /*
    test @RedisParam
     */
    @SneakyThrows
    @Test
    public void test2() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Integer> list = Lists.newArrayList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        for (Integer i : list) {
            int finalI = i;
            executorService.execute(() -> demoService.print2(finalI));
        }

        checkActiveCount((ThreadPoolExecutor) executorService);
    }

    @SneakyThrows
    @Test
    public void test3() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executorService.execute(() -> demoService.print3(finalI));
        }

        checkActiveCount((ThreadPoolExecutor) executorService);
    }

    /**
     * 优雅的关闭线程池
     * 当线程池中的活跃线程数为0时，退出
     */
    public static void checkActiveCount(ThreadPoolExecutor threadPoolExecutor) {
        while (threadPoolExecutor.getActiveCount() > 0) {
        }
        threadPoolExecutor.shutdown();
    }

}
