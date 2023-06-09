package com.happy3friends.toiletmapbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ToiletMapBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToiletMapBackEndApplication.class, args);
    }

}
