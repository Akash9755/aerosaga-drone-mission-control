package com.aerosaga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AerosagaApplication {

	public static void main(String[] args) {

		System.setProperty("user.timezone", "UTC");

		SpringApplication.run(AerosagaApplication.class, args);
	}

}