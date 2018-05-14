package com.qdone.framework.config;

/*import java.util.concurrent.Executor;*/
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 */
@Configuration
@EnableAsync
public class ExecutorConfig {
	
	private int corePoolSize = 10;// 线程池维护线程的最少数量

	private int maxPoolSize = 1000;// 线程池维护线程的最大数量

	private int queueCapacity = 200; // 缓存队列
	
	private int keepAlive = 300;// 允许的空闲时间

	@Bean(name = "taskExecutor")
	/*org.springframework.core.task.TaskExecutor*/
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("taskExecutor-");
		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		// CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 对拒绝task的处理策略
		executor.setKeepAliveSeconds(keepAlive);
		executor.initialize();
		return executor;
	}

}
