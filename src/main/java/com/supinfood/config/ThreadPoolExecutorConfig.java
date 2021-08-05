package com.supinfood.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 咕泡学院，只为更好的你
 * 咕泡学院-Mic: 2082233439
 * http://www.gupaoedu.com
 **/
@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolExecutorConfig {

    @Bean(name = "lotteryServiceExecutor")
    public Executor lotteryServiceExecutor(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(6);
        executor.setMaxPoolSize(12);
        executor.setQueueCapacity(100000);
        executor.setThreadNamePrefix("lottery-service-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        log.info("CorePoolSize={}", executor.getCorePoolSize());
        log.info("MaxPoolSize={}", executor.getMaxPoolSize());


        return executor;
    }
}
