package com.smartcontactmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.smartcontactmanager.*")
public class NewSmartcontactmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewSmartcontactmanagerApplication.class, args);

	}

}
