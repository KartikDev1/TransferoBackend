package com.example.Transfero;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransferoApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();

		System.setProperty("MONGODB_URI", dotenv.get("MONGODB_URI"));
		System.setProperty("APP_BASE_URL", dotenv.get("APP_BASE_URL"));

		SpringApplication.run(TransferoApplication.class, args);
	}

}
