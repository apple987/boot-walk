package com.qdone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动器
 */
@SpringBootApplication
@EnableTransactionManagement
public class StartUpApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(StartUpApplication.class, args);
	}

}
