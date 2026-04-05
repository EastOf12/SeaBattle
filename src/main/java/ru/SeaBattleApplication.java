package ru;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SeaBattleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeaBattleApplication.class, args);
        log.info("Sea battle started");
    }
}