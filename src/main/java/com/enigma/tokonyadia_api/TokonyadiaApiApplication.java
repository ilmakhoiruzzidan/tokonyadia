package com.enigma.tokonyadia_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TokonyadiaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokonyadiaApiApplication.class, args);
	}

}
