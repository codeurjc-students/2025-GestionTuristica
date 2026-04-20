package com.urjc.plushotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PlushotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlushotelApplication.class, args);
    }

}
