package com.example.bai7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.bai7.repository")
@ComponentScan(basePackages = {"com.example.bai7", "com.example.bai7.entity"})
public class Bai7Application {

	public static void main(String[] args) {
		SpringApplication.run(Bai7Application.class, args);
	}

}
