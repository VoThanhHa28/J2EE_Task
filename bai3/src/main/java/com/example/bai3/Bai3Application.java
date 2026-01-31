package com.example.bai3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.bai3")
public class Bai3Application {

	public static void main(String[] args) {
		SpringApplication.run(Bai3Application.class, args);
	}

}
