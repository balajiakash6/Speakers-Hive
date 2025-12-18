package com.speakershive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // <--- THIS ACTIVATES THE AUTOMATIC ROBOT
public class SpeakersHiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpeakersHiveApplication.class, args);
    }
}