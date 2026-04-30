package com.digitalwallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class DigitalWalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(DigitalWalletApplication.class, args);
    }
}
