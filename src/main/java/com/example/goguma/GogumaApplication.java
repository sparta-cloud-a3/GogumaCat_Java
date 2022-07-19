package com.example.goguma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GogumaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GogumaApplication.class, args);
    }
}
