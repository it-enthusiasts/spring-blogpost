package com.exercise.blogpost;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Spring boot startup project TODO: Move the Task Executor to separate class
 * for good design.
 * 
 * @return
 */

@EnableAsync
@SpringBootApplication
public class BlogpostApplication {

	@Value("${pool.core.size:3}")
	private int poolSize;

	@Value("${pool.max.size:3}")
	private int maxPoolSize;

	@Value("${queue.capacity:500}")
	private int queueCapacity;

	public static void main(String[] args) {
		SpringApplication.run(BlogpostApplication.class, args);
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(poolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("BlogpostService-");
		executor.initialize();
		return executor;
	}
}
