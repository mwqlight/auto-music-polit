package com.autospacemusic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.autospacemusic"})
public class AutoMusicBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoMusicBackendApplication.class, args);
    }

}