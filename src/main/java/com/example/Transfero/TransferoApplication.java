package com.example.Transfero;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransferoApplication {

	public static void main(String[] args) {

		// Only load .env in local dev
		if (isLocalEnvironment()) {
			Dotenv dotenv = Dotenv.configure()
					.ignoreIfMissing()
					.load();

			System.setProperty("MONGODB_URI", dotenv.get("MONGODB_URI"));
			System.setProperty("APP_BASE_URL", dotenv.get("APP_BASE_URL"));
		}

		SpringApplication.run(TransferoApplication.class, args);
	}

	private static boolean isLocalEnvironment() {
		String env = System.getenv("RENDER");
		return env == null || env.isEmpty();
	}

}
