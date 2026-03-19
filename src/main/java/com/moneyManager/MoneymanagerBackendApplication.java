package com.moneyManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MoneymanagerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneymanagerBackendApplication.class, args);
	}

}
