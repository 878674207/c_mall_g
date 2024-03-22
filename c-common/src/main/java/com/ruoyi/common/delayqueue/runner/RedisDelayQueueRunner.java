package com.ruoyi.common.delayqueue.runner;


import com.ruoyi.common.delayqueue.enums.RedisDelayQueueEnum;
import com.ruoyi.common.delayqueue.handle.RedisDelayQueueHandle;
import com.ruoyi.common.delayqueue.util.RedisDelayQueueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * description: 启动延迟队列监测扫描
 * @author:
 * date: 2023年6月25日11:37:28
 */
@Slf4j
@Component
public class RedisDelayQueueRunner implements CommandLineRunner {
    @Autowired
    private RedisDelayQueueUtil redisDelayQueueUtil;
    @Autowired
    private ApplicationContext context;

    // 延迟队列任务单独线程池  饱和策略任务丢给主线程
    ThreadPoolExecutor executorService = new ThreadPoolExecutor(50, 100, 30, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000), (Runnable r, ThreadPoolExecutor e) -> {
        if (!e.isShutdown()) {
            r.run();
            log.error("Thread Pool arived maxi mum size");
        }
    });

    @Override
    public void run(String... args) {
        // 监控延迟队列线程常驻，不用线程池
        new Thread(() -> {
            while (true) {
                try {
                    for (RedisDelayQueueEnum queueEnum : RedisDelayQueueEnum.values()) {
                        // 从该种类型的延迟队列取元素
                        Object value = redisDelayQueueUtil.getDelayQueue(queueEnum.getCode());

                        // 元素不为空，从spring容器里面取对应的处理器执行execute方法
                        if (Objects.nonNull(value)) {
                            RedisDelayQueueHandle<Object> redisDelayQueueHandle = (RedisDelayQueueHandle<Object>)context.getBean(queueEnum.getBeanId());
                            executorService.execute(() -> redisDelayQueueHandle.execute(value));
                        }
                    }
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    log.error("(Redission延迟队列监测异常中断) {}", e.getMessage());
                }
            }
        }).start();
        log.info("(Redission延迟队列监测启动成功)");
    }
}