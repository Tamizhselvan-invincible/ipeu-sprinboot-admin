package com.hetero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class HeteroApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeteroApplication.class, args);
	}

}
