package com.zerohub.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程池配置
 *
 * @author zhangyu
 * @date 2022/12/14 14:10
 */
@Configuration
public class AsyncConfig {

    @Value("${executor.core-pool-size}")
    private Integer corePoolSize;
    @Value("${executor.max-pool-size}")
    private Integer maxPoolSize;
    @Value("${executor.queue-capacity}")
    private Integer queueCapacity;
    @Value("${executor.keep-alive-seconds}")
    private Integer keepAliveSeconds;

    @Bean("asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数 -- 线程池创建时初始化的线程数
        executor.setCorePoolSize(corePoolSize);
        // 最大线程数 -- 线程池最大的线程数,只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(maxPoolSize);
        // 缓冲队列 -- 用来换从执行任务等待队列
        executor.setQueueCapacity(queueCapacity);
        // 允许线程的空闲时间 -- 超过了核心线程数之外的线程在空闲时间到达之后将会被摧毁
        executor.setKeepAliveSeconds(keepAliveSeconds);

        // 线程池名的前缀 -- 方便定位处理任务所在的线程池
        executor.setThreadNamePrefix("Async-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 等任务执行完毕之后才会关闭
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
}
