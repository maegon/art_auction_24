package com.example.ArtAuction_24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ArtAuction24Application {
	public static void main(String[] args) {
		SpringApplication.run(ArtAuction24Application.class, args);
	}

}
